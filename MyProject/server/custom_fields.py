import typing
from marshmallow import fields

from .models import session, Customer, Post
from . import ma_sqla, ValidationError


class FieldId(fields.Integer):
	def __init__(self, class_):
		self.class_ = class_
		super().__init__(strict=True, required=True)

	def _validated(self, value):
		if not session.query(self.class_).filter_by(id=value).first():
			return super()._validated(value)
		raise ValidationError("Id was already found in database")


class FieldExistingId(fields.Integer):
	def __init__(self, class_, required=False):
		self.class_ = class_
		super().__init__(strict=True, allow_none=True, required=required)

	def _validated(self, value):
		if session.query(self.class_).filter_by(id=value).first():
			return super()._validated(value)
		raise ValidationError("Id was not found in database")


def customer_id():
	return FieldExistingId(class_=Customer)


def post_id():
	return FieldExistingId(class_=Post)


class FieldEnum(fields.Field):
	def __init__(self, group):
		self.group = group
		super().__init__(required=True)

	def _deserialize(
			self,
			value: typing.Any,
			attr: str | None,
			data: typing.Mapping[str, typing.Any] | None,
			**kwargs,
	):
		try:
			return self.group(str(value))
		except ValueError:
			print("value:")
			print(value)
			print(self.group)
			print("gg")
			print(self.group(str(value)))
			# print(self.group.values())
			# raise ValidationError(str(value) + " is not a valid " + re.search("(?!')\w*(?=')", str(self.group)).group(0))
