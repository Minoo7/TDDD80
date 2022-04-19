import re
import typing

from password_validator import PasswordValidator
from python_phone_validation import AbstractPhoneValidation

import custom_fields
from custom_fields import ValidationError, fields
from models import *
from marshmallow import validates as validator, validate
from usernames import is_safe_username

from server import ma

USERNAME_LENGTH_MIN = 5
NAME_LENGTH_MIN = 3
NAME_LENGTH_MAX = 32

PHONE_VAL_API_KEY = "d3e38e8aa0b4467686db3d073cfe450a"  # Get your API Key from https://app.abstractapi.com/api/phone-validation/documentation

AbstractPhoneValidation.configure(PHONE_VAL_API_KEY)

# --- Nice imports ---
SQLAlchemyAutoSchema = ma.SQLAlchemyAutoSchema


# checks both reserved names and confusable


class LikeSchema(SQLAlchemyAutoSchema):
	Meta = Like.__marshmallow__().Meta

	id = fields.Integer(strict=True, required=True)
	post_id = custom_fields.post_id()
	user_id = custom_fields.customer_id()

	"""@validater('post_id')
	def validate_post_id(self, post_id):
		if post_id < 5:
			raise ValidationError('The age is too old!')"""


class CustomerSchema(SQLAlchemyAutoSchema):
	Meta = Customer.__marshmallow__().Meta

	id = custom_fields.FieldId(Customer)
	username = fields.Str(required=True)
	password = fields.Str(required=True)
	first_name = fields.Str(required=True)
	last_name = fields.Str(required=True)
	email = fields.Email(required=True)
	gender = custom_fields.FieldEnum(Enums.Genders)
	customer_number = fields.Int(required=True)
	phone_number = fields.Str(required=True)
	business_type = custom_fields.FieldEnum(Enums.BusinessTypes)
	organization_number = fields.Int(required=True)  # should be length not max

	@validator('username')
	def validate_username(self, value):
		if not validate_username(value):
			raise ValidationError('username was not a valid username')

	@validator('password')
	def validate_password(self, value):
		if not PasswordValidator().validate(value):
			raise ValidationError('password was not a valid password')

	@validator('first_name')
	def validate_first_name(self, value):
		validate_name('first_name', value)

	@validator('last_name')
	def validate_last_name(self, value):
		validate_name('last_name', value)

	@validator('customer_number')
	def validate_customer_number(self, value):
		if not len(str(value)) == 10:
			raise ValidationError("Customer number must be 10 digits long")

	@validator('phone_number')
	def validate_phone_number(self, value):
		if not AbstractPhoneValidation.verify(value).valid:
			raise ValidationError('invalid phone number')

	@validator('organization_number')
	def validate_organization_number(self, value):
		if not len(str(value)) == 11:
			raise ValidationError("Organization number must be 11 digits")


def validate_username(username):
	return USERNAME_LENGTH_MIN <= len(username) and is_safe_username(username, max_length=NAME_LENGTH_MAX)


def validate_name(name, value):
	if not NAME_LENGTH_MIN <= len(value) <= NAME_LENGTH_MAX and (
			value.isalpha() or (value[0].isupper() and value[1:].isalpha())):
		raise ValidationError(name + " is not a valid name")


def validate_organization_number(number):
	return len(number) == 10


class InputNotValidError(ValueError):

	pass

"""-------------------- CUSTOM FIELDS: --------------------"""


# imps