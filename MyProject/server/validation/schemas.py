import json
import re
import string
import sys
from datetime import datetime

from flask_bcrypt import Bcrypt
from marshmallow_enum import EnumField
from password_validator import PasswordValidator
from .. import groups, app, ma_sqla, ValidationError
from . import custom_fields
from ..models import Model, session, Customer, Address, Comment, Post, ImageReference, Like, Feed, db
from .phoneformat import format_phone_number
from flask_marshmallow import Marshmallow
from marshmallow import validates as validator, validate, validates_schema, post_load, fields, pre_load, post_dump, \
	pre_dump
from usernames import is_safe_username
from MyProject.server.validation.validate import get_current_time, obj_with_attr_exists, id_generator

bcrypt = Bcrypt(app)

ma = Marshmallow(app)

USERNAME_LENGTH_MIN = 5
NAME_LENGTH_MIN = 3
NAME_LENGTH_MAX = 32

my_password_validator = PasswordValidator()

my_password_validator \
	.min(8) \
	.max(100) \
	.has().uppercase() \
	.has().lowercase() \
	.has().digits() \
	.has().no().spaces()

# --- Nice imports ---
SQLAlchemyAutoSchema = ma.SQLAlchemyAutoSchema


def setup_schema():
	# Create a function which incorporates the Base and session information
	def setup_schema_fn():
		# for class_ in models:
		class Meta(object):
			model = None
			sqla_session = session
			include_fk = True
			load_instance = True
			datetimeformat = '%Y-%m-%dT%H:%M:%S%z'

		for class_ in [x.class_ for x in Model.registry.mappers]:
			if hasattr(class_, "__tablename__"):
				meta_class = type("Meta", (Meta,), {"model": class_})

				schema_class_name = "%sSchema" % class_.__name__
				schema_class = type(
					schema_class_name, (ma.SQLAlchemyAutoSchema,), {"Meta": meta_class}
				)

				setattr(class_, "__marshmallow__", schema_class)

	return setup_schema_fn


setup_schema()()


class BaseSchema(SQLAlchemyAutoSchema):
	id = fields.Int()


class CustomerSchema(BaseSchema):
	Meta = Customer.__marshmallow__().Meta

	username = fields.Str(required=True)
	password = fields.Str(required=True, load_only=True)
	first_name = fields.Method(deserialize="capitalize", required=True, load_only=True)
	last_name = fields.Method(deserialize="capitalize", required=True, load_only=True)
	email = fields.Email(required=True, load_only=True)
	gender = EnumField(groups.Genders, load_only=True)
	phone_number = fields.Method(deserialize="remove_unnecessary_chars", required=True, load_only=True)
	business_type = EnumField(groups.BusinessTypes, required=True)
	business_name = fields.Str(required=True)
	organization_number = fields.Int(required=True, load_only=True)
	customer_number = fields.Str(load_only=True, load_default=lambda: CustomerSchema().unique_customer_number())

	class FollowSchema(SQLAlchemyAutoSchema):
		id = fields.Int()
		username = fields.Str()

	# relationships:
	following = fields.Nested(FollowSchema, dump_only=True, many=True)
	followers = fields.Nested(FollowSchema, dump_only=True, many=True)
	posts = fields.Nested(lambda: PostSchema(), many=True)

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
	def add_missing(self, data, **kwargs):
		# data['customer_number'] = self.unique_customer_number()

		# format phone number into swedish standard
		data['phone_number'] = format_phone_number(data['phone_number'])

		data['password'] = bcrypt.generate_password_hash(data['password']).decode('utf-8')
		return data

	@post_dump
	def remove_id(self, data, **kwargs):
		data.pop('id', None)
		data.pop('customer_number', None)
		return data


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


# def get_days_since_created(self, obj):
# 	return datetime.datetime.now().day - obj.created_at.day


class PostSchema(SQLAlchemyAutoSchema):
	Meta = Post.__marshmallow__().Meta

	customer_id = custom_fields.customer_id()
	image_id = custom_fields.FieldExistingId(ImageReference, required=False)
	content = fields.Str(validate=validate.Length(max=120))
	type = EnumField(groups.PostTypes, required=True)

	@validates_schema
	def validate_schema(self, data, **kwargs):
		if not ('content' in data or 'image_id' in data):
			raise ValidationError("Either content or an image is required")


class LikeSchema(SQLAlchemyAutoSchema):
	Meta = Like.__marshmallow__().Meta

	post_id = custom_fields.post_id()
	customer_id = custom_fields.customer_id()


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


def register_schemas():
	"""
	Adds a reference attribute to the corresponding schema for every model class.
	Also adds the required fields used for json validation (used in views.py).
	"""
	for class_ in [x.class_ for x in Model.registry.mappers]:
		txt = str(class_.__name__)
		if txt == "TokenBlocklist":
			continue
		# found_class = eval(str(class_.__name__))
		# print(found_class._validation)
		schema = eval(str(class_.__name__) + "Schema")
		setattr(class_, "__schema__", schema)
	for class_ in [x.class_ for x in Model.registry.mappers]:
		if class_.__name__ == "TokenBlocklist":
			continue
		required_fields = []
		schema_fields = class_.__schema__._declared_fields
		for field in schema_fields:
			if schema_fields[field].required:
				required_fields.append(field)
		setattr(class_, "__required_params__", required_fields)
