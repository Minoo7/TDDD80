# routes..
import os
import sys
from functools import wraps

import werkzeug
from flask import request, jsonify, abort, json, flash, redirect, url_for
from werkzeug.exceptions import HTTPException
from werkzeug.utils import secure_filename

import MyProject.server.validation.schemas
from MyProject.server import app, ValidationError, MYDIR
from MyProject.server.config import ALLOWED_EXTENSIONS
from MyProject.server.constants import GET, POST, DELETE, PUT
from MyProject.server.main import edit_object, find_by_all, find, assert_id_exists
from MyProject.server.models import Customer, session, Address, Post
from MyProject.server.routes.helpers import require_id_exists
from MyProject.server.validation.validate import IdError

MyProject.server.validation.schemas.register_schemas()

# --- Params ---
customer_params = Customer.__required_params__
address_params = Address.__required_params__
post_params = Post.__required_params__


def require_method_params(**outer_kwargs):
	"""
	Decorator function to both assert that the request method is allowed in the request
	and set which json parameters each request requires. If a method is added to the partial tuple
	it then only needs at least one of the listed json parameter
	"""

	def decorator(func):
		@wraps(func)
		def wrapper(*args, **kwargs):
			if request.method in outer_kwargs:
				params = outer_kwargs[request.method]

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

			return func(*args, **kwargs)

		return wrapper

	return decorator


def require_id_exists_2(*outer_args, **outer_kwargs):
	"""
	Decorator function to assert that object of given class type with given id exists in session
	"""

	def decorator(func):
		@wraps(func)
		def wrapper(*args, **kwargs):
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

			return func(*args, **kwargs)

		return wrapper

	return decorator


def require_ownership(**outer_kwargs):
	"""Decorator function to assert that object trying to be edited/deleted is owned by given customer_id"""

	def decorator(func):
		@wraps(func)
		def wrapper(*args, **kwargs):
			# customer_id=["address_id"]



			return func(*args, **kwargs)

		return wrapper

	return decorator


# def init_views(app):

@app.route("/", methods=["GET"])
def home():
	return "Hello World", 200


def allowed_file(filename):
	return '.' in filename and \
		   filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


def allowed_file(filename):
	return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/file-upload', methods=['POST'])
def upload_file():
	# check if the post request has the file part
	if 'file' not in request.files:
		return jsonify(message='No file part in the request'), 400
	file = request.files['file']
	if not file.filename:
		return jsonify(message='No file selected for uploading'), 400
	if file and allowed_file(file.filename):
		filename = secure_filename(file.filename)
		# file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
		file.save(os.path.join(MYDIR + "" + app.config['UPLOAD_FOLDER'], filename))
		return jsonify(message='File successfully uploaded'), 201
	else:
		return jsonify(message='Allowed file types are png, jpg, jpeg'), 400


# CREATE own decorator which sets which methods need jwt_required, later..


@app.route("/customers", methods=[GET, POST])
@require_method_params(POST=customer_params)
def customers():
	if request.method == GET:
		return jsonify([customer.username for customer in find_by_all(Customer)]), 200
	if request.method == POST:
		customer = Customer.__schema__().load(request.json)
		session.add(customer)
		session.commit()
		return jsonify(message="Successfully created customer"), 201


@app.route("/customers/<int:customer_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(customer_params))
@require_id_exists()
def update_customer(customer_id):
	if request.method == PUT:
		edit_object(Customer, customer_id, request.json)
		session.commit()
		return jsonify(message="Successfully edited Customer"), 200
	if request.method == DELETE:
		session.delete(find(Customer, customer_id))
		session.commit()
		return jsonify(message="Successfully deleted Customer"), 200


@app.route("/customers/<int:customer_id>/addresses", methods=[GET, POST])
@require_method_params(POST=address_params)
@require_id_exists()
def addresses(customer_id):
	if request.method == GET:
		customer_address = find(Customer, customer_id).address
		return jsonify(customer_address.street), 200
	if request.method == POST:
		session.add(Address.__schema__().load(request.json | {'customer_id': customer_id}))
		session.commit()
		return jsonify(message="Successfully created address"), 201


@app.route("/customers/<int:customer_id>/addresses/<int:address_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(address_params))
@require_id_exists()
@require_ownership()
def address(customer_id, address_id):
	if request.method == PUT:
		edit_object(Address, address_id, request.json)
		session.commit()
		return jsonify(message="Successfully edited Address"), 200
	if request.method == DELETE:
		session.delete(find(Address, address_id))
		session.commit()
		return jsonify(message="Successfully deleted Address"), 200


@app.route("/customers/<int:customer_id>/posts", methods=[GET, POST])
@require_method_params(POST=post_params)
@require_id_exists(Customer)
def posts():
	if request.method == GET:
		return jsonify([customer.posts for customer in find_by_all(Customer)]), 200
	if request.method == POST:
		pass


@app.route("/upload")
def upload():
	import os
	os.getcwd()
	pass


@app.errorhandler(IdError)
def handle_bad_id(e):
	return jsonify(error=str(e)), 400


@app.errorhandler(ValidationError)
def handle_bad_validation(e):
	return jsonify(error=str(e)), 400


"""@app.errorhandler(werkzeug.exceptions.BadRequest)
def handle_bad_request(e):
	return 'bad request!', 400"""

"""@app.errorhandler(HTTPException)
def handle_exception(e):
	Return JSON instead of HTML for HTTP errors."""
# start with the correct headers and status code from the error
"""response = e.get_response()
	# replace the body with JSON
	response.data = json.dumps({
		"code": e.code,
		"name": e.name,
		"description": e.description,
	})
	response.content_type = "application/json"
return response"""


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
