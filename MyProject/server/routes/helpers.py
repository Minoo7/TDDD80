# --- Params ---
from functools import wraps

from flask import request

from MyProject.server.main import assert_id_exists
from MyProject.server.models import Customer, Address, Post

# customer_params = Customer.__required_params__
# address_params = Address.__required_params__
# post_params = Post.__required_params__


def create_decorator(outer_func):
	"""Function to create new decorator method - without needing to repeat code for creation."""

	def creator(*outer_args, **outer_kwargs):
		def decorator(func):
			@wraps(func)
			def wrapper(*args, **kwargs):
				outer_func(outer_args, outer_kwargs)
				return func(*args, **kwargs)
			return wrapper
		return decorator
	return creator


def require_id_exists_func(*args, **kwargs):
	"""
	Decorator function to assert that object of given class type with given id exists in session
	"""
	# using hard coded string
	# - could also use kwargs and then getting class by String provided and assert dynamically
	"""request_args = list(request.view_args)
	for i in range(len(request_args)):
	assert_id_exists(outer_args[i], request.view_args[request_args[i]])"""

	"""for request_arg in request.view_args:
		if request_arg in outer_kwargs:
		class_ = outer_kwargs[request_arg]
		assert_id_exists(class_, request.view_args[request_arg])
		else:
			abort(400, f"Incorrect wrapper syntax")"""

	# which of these three methods to use?, current one requires the least syntax

	for request_arg in request.view_args:
		class_ = None
		match request_arg:
			case 'customer_id':
				class_ = Customer
			case 'address_id':
				class_ = Address
			case 'post_id':
				class_ = Post
		assert_id_exists(class_, request.view_args[request_arg])

require_id_exists = create_decorator(require_id_exists_func)
