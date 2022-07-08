import collections
import re

from .. import groups, app, ValidationError, session, s3
from . import custom_fields
from ..config import AWS_STORAGE_BUCKET_NAME
from ..models import Model, Customer, Address, Comment, Post, ImageReference, Like
from .validate import unique_customer_number, obj_with_attr_exists, USERNAME_LENGTH_MIN, NAME_LENGTH_MIN, \
	NAME_LENGTH_MAX, my_password_validator
from .phoneformat import format_phone_number

from flask_marshmallow import Marshmallow
from marshmallow import validates as validator, validate, validates_schema, post_load, fields, post_dump
from flask_bcrypt import Bcrypt

from marshmallow_enum import EnumField
from usernames import is_safe_username

bcrypt = Bcrypt(app)
ma = Marshmallow(app)

# --- Nice imports ---
SQLAlchemyAutoSchema = ma.SQLAlchemyAutoSchema


"""class formats:
	Follower = lambda: CustomerSchema(only=["id", "username"])
	# Like = lambda: LikeSchema(only=["id"])
"""

formats = collections.namedtuple("TupleOfCustomFormats", ("Follower"))(lambda: CustomerSchema(only=["id", "username"]))


def setup_schemas():
	# generate individual base meta to be used for every schema class
	class Meta(object):
		model = None
		sqla_session = session
		include_fk = True
		load_instance = True

	for class_ in [x.class_ for x in Model.registry.mappers]:
		if hasattr(class_, "__tablename__"):
			meta_class = type("Meta", (Meta,), {"model": class_})
			schema_class_name = "%sSchema" % class_.__name__
			schema_class = type(schema_class_name, (ma.SQLAlchemyAutoSchema,), {"Meta": meta_class})
			setattr(class_, "__marshmallow__", schema_class)


setup_schemas()


class PostSchema(SQLAlchemyAutoSchema):
	Meta = Post.__marshmallow__().Meta

	customer_id = custom_fields.customer_id()
	image_id = custom_fields.FieldExistingId(ImageReference, required=False)
	title = fields.Str(validate=validate.Length(max=40), required=True)
	content = fields.Str(validate=validate.Length(max=120))
	type = EnumField(groups.PostTypes, required=True)
	created_at = fields.DateTime(dump_only=True)

	# relationships:
	likes = fields.Nested(lambda: LikeSchema, dump_only=True, many=True)
	comments = fields.Nested(lambda: CommentSchema, dump_only=True, many=True)

	@validates_schema
	def validate_schema(self, data, **kwargs):
		if not ('content' in data or 'image_id' in data):
			raise ValidationError("Either content or an image is required")

	@post_dump
	def add_image_reference(self, data, **kwargs):
		image_id_ = data.pop('image_id', None)

		if image_id_ is not None:
			url = s3.generate_presigned_url(
				ClientMethod='get_object',
				Params={
					'Bucket': AWS_STORAGE_BUCKET_NAME,
					'Key': ImageReference.query.get(image_id_).path
				}
			)
			data['image_url'] = url
		return data


class BaseSchema(SQLAlchemyAutoSchema):
	id = fields.Int()


class CustomerSchema(BaseSchema):
	Meta = Customer.__marshmallow__().Meta

	username = fields.Str(required=True)
	password = fields.Str(required=True, load_only=True)
	first_name = custom_fields.Custom(load="capitalize", required=True)
	last_name = custom_fields.Custom(load="capitalize", required=True)
	email = fields.Email(required=True)
	gender = EnumField(groups.Genders, load_only=True)
	phone_number = custom_fields.Custom(load=lambda val: re.sub('[^\d+]+', '', val), required=True)
	business_type = EnumField(groups.BusinessTypes, required=True)
	business_name = fields.Str(required=True)
	bio = fields.Str()
	organization_number = fields.Int(required=True)
	customer_number = fields.Str(load_default=lambda: unique_customer_number())
	# add validation:....
	image_id = custom_fields.FieldExistingId(class_=ImageReference)

	# relationships:
	following = fields.Nested(formats.Follower, dump_only=True, many=True)
	followers = fields.Nested(formats.Follower, dump_only=True, many=True)
	posts = fields.Nested(PostSchema, many=True)

	@staticmethod
	def capitalize(value):
		return value.title()

	@validator('username')
	def validate_username(self, value):
		if not (USERNAME_LENGTH_MIN <= len(value) and is_safe_username(value, max_length=NAME_LENGTH_MAX)):
			raise ValidationError('username was not a valid username')
		if obj_with_attr_exists(Customer, 'username', value):
			raise ValidationError("Customer with this username already exists")

	@validator('password')
	def validate_password(self, value):
		if not my_password_validator.validate(value):
			raise ValidationError('password was not a valid password')

	@staticmethod
	def is_valid_name(name):
		return NAME_LENGTH_MIN <= len(name) <= NAME_LENGTH_MAX and name.isalpha()

	@validator('first_name')
	def validate_first_name(self, value):
		if not self.is_valid_name(value):
			raise ValidationError("first name: " + value + " is not a valid name")

	@validator('last_name')
	def validate_last_name(self, value):
		if not self.is_valid_name(value):
			raise ValidationError("last name: " + value + " is not a valid name")

	@validator('email')
	def validate_email(self, value):
		if obj_with_attr_exists(Customer, 'email', value):
			raise ValidationError("Customer with this email already exists")

	@validator('phone_number')
	def validate_phone_number(self, value):
		if re.match('\+?(?:0{0,2}[46]*){1}7{1}[0-9]{8}', value) is None:
			raise ValidationError('invalid phone number')
		if obj_with_attr_exists(Customer, 'phone_number', format_phone_number(value)):
			raise ValidationError("Customer with this phone number already exists")

	@validator('business_name')
	def validate_business_name(self, value):
		if not (2 <= len(value) <= 50):
			raise ValidationError("Business name must be between 2 and 50 characters long")
		if obj_with_attr_exists(Customer, 'business_name', value):
			raise ValidationError("Customer with this Business name already exists")

	@validator('bio')
	def validate_bio(self, value):
		if len(value) > 120:
			raise ValidationError("Bio can't be longer than 120 characters long!")

	@validator('organization_number')
	def validate_organization_number(self, value):
		if len(str(value)) != 11:
			raise ValidationError("Organization number must be 11 digits")
		if obj_with_attr_exists(Customer, 'organization_number', value):
			raise ValidationError("Customer with this organization_number already exists")

	@post_load
	def format_fields(self, data, **kwargs):
		# format phone number into swedish standard
		data['phone_number'] = format_phone_number(data['phone_number'])

		data['password'] = bcrypt.generate_password_hash(data['password']).decode('utf-8')
		return data

	@post_dump
	def add_image_reference(self, data, **kwargs):
		image_id_ = data.pop('image_id', None)

		if image_id_ is not None:
			url = s3.generate_presigned_url(
				ClientMethod='get_object',
				Params={
					'Bucket': AWS_STORAGE_BUCKET_NAME,
					'Key': ImageReference.query.get(image_id_).path
				}
			)
			data['image_url'] = url
		return data


class AddressSchema(SQLAlchemyAutoSchema):
	Meta = Address.__marshmallow__().Meta

	address_type = EnumField(groups.AddressTypes, required=True)
	street = fields.Str(required=True)
	city = fields.Str(required=True)
	zip_code = fields.Str(required=True)
	other_info = fields.Str(allow_none=True, validate=validate.Length(max=255))
	customer_id = custom_fields.customer_id()

	@validator('street')
	def validate_street(self, value):
		d = re.match("^[åäöÅÄÖA-Za-z_]+ [0-9]+$", value)
		if not (len(value) <= 95 and d):
			raise ValidationError("improper address")

	@validator('city')
	def validate_city(self, value):
		if not (2 <= len(value) <= 35 and value.isalpha()):
			raise ValidationError("improper city")

	@validator('zip_code')
	def validate_zip_code(self, value):
		if not re.match("^\d{3}\s{0,1}\d{2}$", value):
			raise ValidationError("improper zip-code")


class CommentSchema(SQLAlchemyAutoSchema):
	Meta = Comment.__marshmallow__().Meta

	content = fields.Str(validate=validate.Length(max=120), required=True)
	post_id = custom_fields.post_id()
	customer_id = custom_fields.customer_id()


class LikeSchema(BaseSchema):
	Meta = Like.__marshmallow__().Meta

	post_id = custom_fields.post_id()
	customer_id = custom_fields.customer_id()

	"""@validates_schema
	def validate_schema(self, data, **kwargs):
		if not ('post_id' in data and 'customer_id' in data):
			raise ValidationError("Either content or an image is required")"""


class ImageReferenceSchema(SQLAlchemyAutoSchema):
	Meta = ImageReference.__marshmallow__().Meta

	path = fields.Str(required=True)

	@validator('path')
	def validate_path(self, value):
		# add validation of proper path...
		if len(value) > 120:
			raise ValidationError("length of path can at max be 120 characters long")


def register_schemas():
	"""
	Adds a reference attribute to the corresponding schema for every model class.
	Also adds the required fields used for json validation (used in views.py).
	"""
	for class_ in [x.class_ for x in Model.registry.mappers]:
		if class_._validation:
			# add schema class
			schema = eval(str(class_.__name__) + "Schema")
			setattr(class_, "__schema__", schema)

			# adding required params from required attribute in schema class
			schema_fields = schema._declared_fields
			required_fields = [field for field in schema_fields if schema_fields[field].required]
			setattr(class_, "__required_params__", required_fields)
			setattr(class_, "__params__", schema_fields)
