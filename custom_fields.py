from flask_marshmallow import fields
from marshmallow import ValidationError

from models import *


class FieldId(fields.Integer):
	def __init__(self, class_):
		self.class_ = class_
		super().__init__(strict=True)

	def _validated(self, value):
		print(value)
		if db.session.query(self.class_).filter_by(id=value).first():
			return super()._validated(value)
		raise ValidationError("Id was not found in database")


def customer_id():
	return FieldId(class_=Customer)


def post_id():
	return FieldId(class_=Post)
