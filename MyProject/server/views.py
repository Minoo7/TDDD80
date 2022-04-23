# routes..
from functools import wraps

from flask import request, jsonify, abort

from MyProject.server import app, models
from MyProject.server.models import Customer, session


def require_method_params(*outer_args, **outer_kwargs):
	"""
	Decorator function to both assert that the request method is allowed in the request
	and set which json parameters each request requires.
	"""

	def decorator(func):
		@wraps(func)
		def wrapper(*args, **kwargs):

			if request.method not in outer_kwargs:
				abort(400, "Method not allowed")

			required_params = outer_kwargs[request.method]

			if len(required_params) > 0 and not hasattr(request, 'json'):
				abort(400, "Missing json.")

			for param in required_params:
				if param not in request.json:
					abort(400, f"Missing json parameter. Required: {required_params}")

			return func(*args, **kwargs)

		return wrapper

	return decorator


# def init_views(app):

@app.route("/", methods=["GET"])
def home():
	return "Hello World", 200


@app.route("/customers", methods=["GET"])
@require_method_params(GET=[])
def get_customers():
	if request.method == "GET":
		return session.query(Customer).first().username, 200
# return jsonify([msg.to_dict() for msg in Message.query.all()]), 200"""
