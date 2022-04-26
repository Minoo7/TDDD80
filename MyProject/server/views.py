# routes..
from functools import wraps

import werkzeug
from flask import request, jsonify, abort, json
from werkzeug.exceptions import HTTPException

from MyProject.server import app, ValidationError
from MyProject.server.constants import GET, POST, DELETE, PUT
from MyProject.server.models import Customer, session
from MyProject.server.validation.schemas import CustomerSchema, AddressSchema
from MyProject.server.validation.validate import IdError, parse_id


def query(class_, **attr):
	if 'id' in attr:
		attr['id'] = parse_id(class_, attr['id'])

	return session.query(class_).filter_by(**attr)


def find(class_, **kwargs):
	return query(class_, **kwargs).first()


def find_all(class_, **kwargs):
	return query(class_, **kwargs).all()


def require_method_params(partial=tuple(), **outer_kwargs):
	"""
	Decorator function to both assert that the request method is allowed in the request
	and set which json parameters each request requires. If a method is added to the partial tuple
	it then only needs at least one of the listed json parameter
	"""

	def decorator(func):
		@wraps(func)
		def wrapper(*args, **kwargs):

			if request.method not in outer_kwargs:
				abort(400, "Method not allowed")

			required_params = outer_kwargs[request.method]

			if len(required_params) > 0 and not hasattr(request, 'json'):
				abort(400, "Missing json.")

			if request.method in partial:
				for param in request.json:
					if param not in required_params:
						abort(400, f"Json parameter provided was not allowed. Allowed: {required_params}")
			else:
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

# CREATE own decorator which sets which methods need jwt_required, later..


@app.route("/customers", methods=[GET, POST])
@require_method_params(GET=[], POST=["username", "password", "first_name", "last_name", "email", "gender", "phone_number", "business_type",
		  "organization_number"])
def customers():
	if request.method == GET:
		return jsonify([customer.username for customer in find_all(Customer)]), 200
	if request.method == POST:
		customer = CustomerSchema().load(request.json)
		session.add(customer)
		session.commit()
		return jsonify(message="Successfully created customer"), 201


@app.route("/customers/<customerId>", methods=[PUT, DELETE])
@require_method_params(partial=PUT, PUT=["username", "password", "first_name", "last_name", "email", "gender", "phone_number", "business_type",
		  "organization_number"], DELETE=[])
def customer(customerId):
	if request.method == PUT:
		json_input = request.json
		"""for param in json_input:
			print("param: ")
			print(param)
			print(json_input[param])
			print(json_input.items()[0])
			errors = CustomerSchema().load(json_input[param], partial=True)
			print(errors)
			return errors, 200"""

		# if
		CustomerSchema().partial_load(json_input)
		
		#customer = session.query(Customer).get(customerId)
		#for param in json_input:
		#	setattr(customer, param, json_input[param])
		return "", 200



@app.route("/customers/<customerId>/address", methods=[GET, POST])
@require_method_params(GET=[], POST=["address_type", "street", "city", "zip_code"])
def address(customerId):
	if request.method == GET:
		# customer_address = find(Customer, id=customerId).address
		customer_address = session.query(Customer).get(customerId).address
		return jsonify(customer_address.street), 200
	if request.method == POST:
		session.add(AddressSchema().load(request.json | {'customer_id': parse_id(Customer, customerId)}))
		session.commit()
		return jsonify(message="Successfully created address"), 201


@app.errorhandler(IdError)
def handle_bad_id(e):
	return jsonify(error=str(e)), 400


@app.errorhandler(ValidationError)
def handle_bad_validation(e):
	return jsonify(error=str(e)), 400


@app.errorhandler(werkzeug.exceptions.BadRequest)
def handle_bad_request(e):
	return 'bad request!', 400


@app.errorhandler(HTTPException)
def handle_exception(e):
	"""Return JSON instead of HTML for HTTP errors."""
	# start with the correct headers and status code from the error
	response = e.get_response()
	# replace the body with JSON
	response.data = json.dumps({
		"code": e.code,
		"name": e.name,
		"description": e.description,
	})
	response.content_type = "application/json"
	return response


@app.errorhandler(404)
def resource_not_found(e):
	return jsonify(error=str(e)), 404


@app.route("/cheese")
def get_one_cheese():
	resource = "get_resource()"

	if resource is None:
		abort(404, description="Resource not found")

	return jsonify(resource)

# return jsonify([msg.to_dict() for msg in Message.query.all()]), 200"""
