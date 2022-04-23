import re
import typing
from marshmallow import fields
from models import db

import models

ValidationError = models.ma_sqla.ValidationError


class FieldId(fields.Integer):
	def __init__(self, class_):
		self.class_ = class_
		super().__init__(strict=True, required=True)

	def _validated(self, value):
		if not db.session.query(self.class_).filter_by(id=value).first():
			return super()._validated(value)
		raise ValidationError("Id was already found in database")


class FieldExistingId(fields.Integer):
	def __init__(self, class_, required=False):
		self.class_ = class_
		super().__init__(strict=True, allow_none=True, required=required)

	def _validated(self, value):
		if db.session.query(self.class_).filter_by(id=value).first():
			return super()._validated(value)
		raise ValidationError("Id was not found in database")


def customer_id():
	return FieldExistingId(class_=models.Customer)


def post_id():
	return FieldExistingId(class_=models.Post)


class FieldEnum(fields.Field):
	def __init__(self, enum):
		self.enum = enum
		super().__init__(required=True)

	def _deserialize(
			self,
			value: typing.Any,
			attr: str | None,
			data: typing.Mapping[str, typing.Any] | None,
			**kwargs,
	):
		try:
			return self.enum(value)
		except ValueError:
			raise ValidationError(str(value) + " is not a valid " + re.search("(?!')\w*(?=')", str(self.enum)).group(0))
