import re
import string

from marshmallow_enum import EnumField
from password_validator import PasswordValidator
from MyProject.server import groups, app, ma_sqla, ValidationError
from MyProject.server.validation import custom_fields
from MyProject.server.models import Model, session, Customer, Address, Comment, Post, ImageReference, Like, Feed, db
from MyProject.server.validation.phoneformat import format_phone_number
from flask_marshmallow import Marshmallow
from marshmallow import validates as validator, validate, validates_schema, post_load, fields
from usernames import is_safe_username

from MyProject.server.validation.validate import get_current_time, obj_with_attr_exists, id_generator

ma = Marshmallow(app)

USERNAME_LENGTH_MIN = 5
NAME_LENGTH_MIN = 3
NAME_LENGTH_MAX = 32

# --- Nice imports ---
SQLAlchemyAutoSchema = ma.SQLAlchemyAutoSchema


def setup_schema():
	# Create a function which incorporates the Base and session information
	def setup_schema_fn():
		# for class_ in models:
		for class_ in [x.class_ for x in Model.registry.mappers]:
			if hasattr(class_, "__tablename__"):
				if class_.__name__.endswith("Schema"):
					raise ma.sql.ModelConversionError(
						"For safety, setup_schema can not be used when a"
						"Model class ends with 'Schema'"
					)

				class Meta(object):
					model = class_
					sqla_session = session
					include_fk = True
					load_instance = True

				# transient = True

				class Opts(ma_sqla.SQLAlchemyAutoSchemaOpts):
					def __init__(self, meta, ordered=False):
						# if not hasattr(meta, "sqla_session"):
						# 	meta.sqla_session = Session
						"""meta.model = class_
						meta.sqla_session = session
						meta.include_fk = True
						meta.load_instance = True"""
						meta = Meta
						super().__init__(meta, ordered=ordered)

				Opts.__name__ = class_.__name__ + "Opts"

				schema_class_name = "%sSchema" % class_.__name__

				schema_class = type(
					schema_class_name, (ma.SQLAlchemyAutoSchema,), {"Meta": Meta}
				)

				setattr(class_, "__opts__", Opts)
				setattr(class_, "__marshmallow__", schema_class)

	return setup_schema_fn


setup_schema()()


class MySchema(SQLAlchemyAutoSchema):
	def __init__(self, *args, **kwargs):
		self.partial = False
		super().__init__(*args, **kwargs)


class CustomerSchema(MySchema):
	Meta = Customer.__marshmallow__().Meta

	username = fields.Str(required=True)
	password = fields.Str(required=True)
	first_name = fields.Method(deserialize="capitalize", required=True)
	last_name = fields.Method(deserialize="capitalize", required=True)
	email = fields.Email(required=True)
	gender = EnumField(groups.Genders)
	phone_number = fields.Method(deserialize="remove_unnecessary_chars", required=True)
	business_type = EnumField(groups.BusinessTypes, required=True)
	organization_number = fields.Int(required=True)  # should be length not max

	def partial_load(self, json_input):
		self.partial = True
		self.load(json_input, partial=True)

	@staticmethod
	def capitalize(value):
		return value.title()

	# removes everything except digits and '+'
	@staticmethod
	def remove_unnecessary_chars(value):
		return re.sub('[^\d+]+', '', value)

	@validator('username')
	def validate_username(self, value):
		if not (USERNAME_LENGTH_MIN <= len(value) and is_safe_username(value, max_length=NAME_LENGTH_MAX)):
			raise ValidationError('username was not a valid username')
		if obj_with_attr_exists(Customer, 'username', value):
			raise ValidationError("Customer with this username already exists")

	@validator('password')
	def validate_password(self, value):
		if not PasswordValidator().validate(value):
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

	@validator('phone_number')
	def validate_phone_number(self, value):
		if re.match('\+?(?:0{0,2}[46]*){1}7{1}[0-9]{8}', value) is None:
			raise ValidationError('invalid phone number')
		if obj_with_attr_exists(Customer, 'phone_number', value):
			raise ValidationError("Customer with this phone number already exists")

	@validator('organization_number')
	def validate_organization_number(self, value):
		if len(str(value)) != 11:
			raise ValidationError("Organization number must be 11 digits")
		if obj_with_attr_exists(Customer, 'organization_number', value):
			raise ValidationError("Customer with this organization_number already exists")

	# generate unique customer_number
	@staticmethod
	def unique_customer_number():
		def generator():
			return id_generator(size=3, chars=string.ascii_uppercase) + id_generator(size=3, chars=string.digits)

		generated_id = generator()
		while obj_with_attr_exists(Customer, 'customer_number', generated_id):
			generated_id = generator()
		return generated_id

	@post_load
	def make_customer(self, data, **kwargs):
		if self.partial:
			return True
		else:
			data['customer_number'] = self.unique_customer_number()

			# format phone number into swedish standard
			data['phone_number'] = format_phone_number(data['phone_number'])
			return data


# --- Address ---

class AddressSchema(SQLAlchemyAutoSchema):
	Meta = Address.__marshmallow__().Meta

	# fix enum later
	address_type = EnumField(groups.AddressTypes, required=True)
	street = fields.Str(required=True)
	city = fields.Str(required=True)
	zip_code = fields.Str(required=True)
	other_info = fields.Str(allow_none=True, validate=validate.Length(max=255))
	customer_id = custom_fields.customer_id()

	@validator('street')
	def validate_street(self, value):
		d = re.match("^[A-Za-z]+ [0-9]+$", value)
		if not (len(value) <= 95 and d):
			raise ValidationError("improper address")

	@validator('city')
	def validate_city(self, value):
		if not (len(value) <= 35 and value.isalpha()):
			raise ValidationError("improper city")

	@validator('zip_code')
	def validate_zip_code(self, value):
		if not re.match("^\d{3}\s{0,1}\d{2}$", value):
			raise ValidationError("improper zip-code")


class CommentSchema(SQLAlchemyAutoSchema):
	Meta = Comment.__marshmallow__().Meta

	content = fields.Str(validate=validate.Length(max=120), required=True)
	post_id = custom_fields.post_id()
	user_id = custom_fields.customer_id()

	@post_load
	def make_comment(self, data, **kwargs):
		# generate created_at date:
		data['created_at'] = get_current_time()

		return data


# def get_days_since_created(self, obj):
# 	return datetime.datetime.now().day - obj.created_at.day


class PostSchema(SQLAlchemyAutoSchema):
	Meta = Post.__marshmallow__().Meta

	user_id = custom_fields.customer_id()
	image_id = custom_fields.FieldExistingId(ImageReference, required=False)
	content = fields.Str(validate=validate.Length(max=120))
	type = EnumField(groups.PostTypes, required=True)

	@validates_schema
	def validate_schema(self, data, **kwargs):
		if data['content'] is None and data['image_id'] is None:
			raise ValidationError("Either content or an image is required")

	@post_load
	def make_post(self, data, **kwargs):
		data['created_at'] = get_current_time()

		return data


class LikeSchema(SQLAlchemyAutoSchema):
	Meta = Like.__marshmallow__().Meta

	post_id = custom_fields.post_id()
	user_id = custom_fields.customer_id()


class ImageReferenceSchema(SQLAlchemyAutoSchema):
	Meta = ImageReference.__marshmallow__().Meta

	path = fields.Str(required=True)

	@validator('path')
	def validate_path(self, value):
		# add validation of proper path...
		if len(value) > 120:
			raise ValidationError("length of path can at max be 120 characters long")


class FeedSchema(SQLAlchemyAutoSchema):
	Meta = Feed.__marshmallow__().Meta

	customer_id = custom_fields.customer_id()
