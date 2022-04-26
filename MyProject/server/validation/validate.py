import random
import pytz  # as pytz
from datetime import datetime
from .. import ValidationError
from ..models import session


class IdError(ValidationError):
	default_msg = " with this id does not exist"

	def __init__(self, data, class_=None, message=default_msg):
		self.message = message
		self.class_ = class_
		super().__init__(message=self.message, data=data)

	def __str__(self):
		return self.class_.__name__ + self.message if self.message == IdError.default_msg else self.message


def parse_id(class_, id_value):
	id_ = id_value
	if isinstance(id_value, str):
		try:
			id_ = int(id_value)
		except ValueError:
			raise IdError(data=id_value, message="Id must be an Integer")
	if not obj_with_attr_exists(class_, "id", id_):
		raise IdError(id_value, class_)
	return id_


def obj_with_attr_exists(class_, attribute, value):
	filters = {attribute: value}
	return session.query(class_).filter_by(**filters).first() is not None


def get_current_time():
	return datetime.now(pytz.timezone('utc'))


def id_generator(size, chars):
	return ''.join(random.choice(chars) for _ in range(size))
