import random
import pytz  # as pytz
from datetime import datetime
from .. import ValidationError
from ..models import session


class IdError(Exception):
	default_msg = " with this id does not exist"

	def __init__(self, class_=None, id_=None, message=None):
		self.class_ = class_
		self.id_ = id_
		self.message = message
		super().__init__()

	def __str__(self):
		if self.message:
			return self.message
		if self.class_ and self.id_:
			return self.class_.__name__ + " with id: " + str(self.id_) + " does not exist"
		return "id does not exist"


def obj_with_attr_exists(class_, attribute, value):
	filters = {attribute: value}
	return session.query(class_).filter_by(**filters).first() is not None


def get_current_time():
	return datetime.now(pytz.timezone('utc'))


def id_generator(size, chars):
	return ''.join(random.choice(chars) for _ in range(size))
