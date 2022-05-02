from functools import wraps

from flask import request, abort
from flask_jwt_extended import get_jwt_identity

from MyProject.server.main import assert_id_exists, find
from MyProject.server.models import Customer, Address, Post, Like, Comment
from MyProject.server.validation.schemas import register_schemas

register_schemas()

# --- Params ---
customer_params = Customer.__required_params__
address_params = Address.__required_params__
post_params = Post.__required_params__
like_params = Like.__required_params__
comment_params = Comment.__required_params__

arg_class_map = {'customer_id': Customer, 'address_id': Address, 'post_id': Post, 'like_id': Like}


def generate_decorator(outer_func):
	"""Function to create new decorator method - without needing to repeat code for creation."""

	def generator(*outer_args, **outer_kwargs):
		def decorator(func):
			@wraps(func)
			def wrapper(*args, **kwargs):
				outer_func(*outer_args, **outer_kwargs)
				return func(*args, **kwargs)

			return wrapper
		return decorator
	return generator


def require_method_params(**outer_kwargs):
	"""
	Decorator function to both assert that the request method is allowed in the request
	and set which json parameters each request requires. If a method is added to the partial tuple
	it then only needs at least one of the listed json parameter
	"""

	def inner(**kwargs):
		if request.method in kwargs:
			params = kwargs[request.method]
			if len(params) > 0 and not hasattr(request, 'json'):
				abort(400, "Missing json.")
			if isinstance(params, set):
				for param in request.json:
					if param not in params:
						abort(400, f"Json parameter provided was not allowed. Allowed: {params}")
			elif isinstance(params, list):
				for param in params:
					if param not in request.json:
						abort(400, f"Missing json parameter. Required: {params}")

	return generate_decorator(inner)(**outer_kwargs)


def check_id_exists():
	for request_arg in request.view_args:
		if request_arg not in arg_class_map:
			raise KeyError("improper argument name in route")
		assert_id_exists(arg_class_map.get(request_arg), request.view_args[request_arg])


def require_id_exists():
	"""Decorator function to assert that object of given class type with given id exists in session"""

	def inner():
		check_id_exists()

	return generate_decorator(inner)()


"""def check_ownership(**kwargs):
	for owner in kwargs:
		if owner == "current":
			owner_obj = find(Customer, get_jwt_identity())
		else:
			if owner not in request.view_args:
				abort(400, "owner keyword is not inside the route address arguments")
			if owner not in arg_class_map:
				abort(400, "improper owner name in given")

			owner_obj = find(arg_class_map.get(owner), request.view_args[owner])
		for item in kwargs[owner]:
			item_obj = find(arg_class_map.get(item), request.view_args[item])
			getattr(item_obj, owner)
			if item_obj.customer_id != owner_obj.id:
				abort(403, f"Customer did not have permission/own to access the object")"""


def require_hierarchy(**outer_kwargs):
	return generate_decorator(require_hierarchy_func)(**outer_kwargs)


def require_hierarchy_func(**kwargs):
	for owner in kwargs:
		if owner not in request.view_args and owner != "customer_id":
			abort(400, "argument for owner is not inside the route address arguments")
		if owner not in arg_class_map:
			abort(400, "improper owner name in given")

		if owner == "customer_id" and "customer_id" not in request.view_args:
			owner_value = get_jwt_identity()
		else:
			owner_value = request.view_args[owner]

		for item in kwargs[owner]:
			item_obj = find(arg_class_map.get(item), request.view_args[item])
			if getattr(item_obj, owner) != owner_value:
				abort(403, f"Customer did not have permission/own to access the object")



def require_ownership(**outer_kwargs):
	"""Decorator function to assert that id's exist and assert that object trying to be edited/deleted is owned by given customer_id
	syntax: owner=[*'item_id'] also checks that jwt identity equals to customer_id"""

	def inner(**kwargs):
		check_id_exists()
		if 'customer_id' in request.view_args:
			if get_jwt_identity() != request.view_args['customer_id']:
				abort(400, "id in request not corresponding to logged in users id")
		require_hierarchy_func(**kwargs)
	return generate_decorator(inner)(**outer_kwargs)


