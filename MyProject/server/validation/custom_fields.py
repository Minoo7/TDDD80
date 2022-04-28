import typing
from marshmallow import fields

from MyProject.server.main import find, assert_id_exists
from MyProject.server.models import session, Customer, Post
from MyProject.server.validation.validate import IdError


class FieldExistingId(fields.Integer):
	def __init__(self, class_, required=False):
		self.class_ = class_
		super().__init__(strict=True, allow_none=True, required=required)

	def _validated(self, value):
		assert_id_exists(self.class_, value)
		return super()._validated(value)


def customer_id():
	return FieldExistingId(class_=Customer)


def post_id():
	return FieldExistingId(class_=Post)
