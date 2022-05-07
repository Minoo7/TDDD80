import random
import string

import pytz  # as pytz
from datetime import datetime

from password_validator import PasswordValidator

from ... import server
from .. import session

# --- Constants ---
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
	return datetime.now()  # datetime.now(pytz.timezone('utc'))


def id_generator(size, chars):
	return ''.join(random.choice(chars) for _ in range(size))


# generate unique customer_number
def unique_customer_number():
	def generator():
		return id_generator(size=3, chars=string.ascii_uppercase) + id_generator(size=3, chars=string.digits)

	generated_id = generator()
	while obj_with_attr_exists(server.models.Customer, 'customer_number', generated_id):
		generated_id = generator()
	return generated_id
