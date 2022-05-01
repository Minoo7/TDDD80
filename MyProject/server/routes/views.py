# routes..
import os
import sys
from functools import wraps

import werkzeug
from flask import request, jsonify, abort, json, flash, redirect, url_for
from werkzeug.exceptions import HTTPException
from werkzeug.utils import secure_filename

from MyProject.server import app, ValidationError, MYDIR
from MyProject.server.config import ALLOWED_EXTENSIONS
from MyProject.server.constants import GET, POST, DELETE, PUT
from MyProject.server.main import edit_obj, find_by_all, find, assert_id_exists, create_obj, delete_obj
from MyProject.server.models import Customer, session, Address, Post, ImageReference, Like
from MyProject.server.routes.helpers import require_method_params, require_id_exists, customer_params, address_params, \
	post_params, require_ownership
from MyProject.server.validation.validate import IdError


@app.route("/", methods=["GET"])
def home():
	return "Hello World", 200


def allowed_file(filename):
	return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


# CREATE own decorator which sets which methods need jwt_required, later..


@app.route("/customers", methods=[GET, POST])
@require_method_params(POST=customer_params)
def customers():
	if request.method == GET:
		return jsonify([customer.username for customer in find_by_all(Customer)]), 200
	if request.method == POST:
		create_obj(Customer, request.json)
		return jsonify(message="Successfully created customer"), 201


@app.route("/customers/<int:customer_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(customer_params))
@require_id_exists()
def update_customer(customer_id):
	if request.method == PUT:
		edit_obj(Customer, customer_id, request.json)
		return jsonify(message="Successfully edited Customer"), 200
	if request.method == DELETE:
		delete_obj(Customer, customer_id)
		return jsonify(message="Successfully deleted Customer"), 200


@app.route("/customers/<int:customer_id>/addresses", methods=[GET, POST])
@require_method_params(POST=address_params)
@require_id_exists()
def addresses(customer_id):
	if request.method == GET:
		customer_address = find(Customer, customer_id).address
		return jsonify(customer_address.street), 200
	if request.method == POST:
		# create_obj(Address, request.json | {'customer_id': customer_id})
		create_obj(Address, request.json | dict(customer_id=customer_id))
		return jsonify(message="Successfully created address"), 201


@app.route("/customers/<int:customer_id>/addresses/<int:address_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(address_params))
@require_id_exists()
@require_ownership(customer_id=['address_id'])
def address(customer_id, address_id):
	if request.method == PUT:
		edit_obj(Address, address_id, request.json)
		return jsonify(message="Successfully edited address"), 200
	if request.method == DELETE:
		delete_obj(Address, address_id)
		return jsonify(message="Successfully deleted address"), 200


@app.route("/customers/<int:customer_id>/images", methods=[POST])
@require_id_exists()
def images(customer_id):
	if request.method == POST:
		if 'file' not in request.files:
			return jsonify(message='No file part in the request'), 400

		file = request.files['file']
		if not file.filename:
			return jsonify(message='No file selected for uploading'), 400

		upload_path = os.path.join(os.getcwd() + app.config['UPLOAD_FOLDER'])
		user_folder = os.path.join(upload_path + str(customer_id))
		if not os.path.isdir(user_folder):
			os.mkdir(user_folder)

		if file and allowed_file(file.filename):
			filename = secure_filename(file.filename)
			file_path = os.path.join(user_folder, filename)
			file.save(file_path)
			create_obj(ImageReference, {'path': file_path})
			return jsonify(message='File successfully uploaded'), 201
		else:
			return jsonify(message='Allowed file types are png, jpg, jpeg'), 400


# https://www.moesif.com/blog/technical/api-design/REST-API-Design-Best-Practices-for-Sub-and-Nested-Resources/
# Using security approach for images - not using user_id as parameter for images
# or ?

@app.route("/customers/<int:customer_id>/posts", methods=[GET, POST])
@require_method_params(POST=post_params)
@require_id_exists()
def posts(customer_id):
	if request.method == GET:
		return jsonify([customer.posts for customer in find_by_all(Customer)]), 200
	if request.method == POST:
		create_obj(Post, request.json | dict(user_id=customer_id))
		return jsonify(message="Successfully created post"), 201


@app.route("/customers/<int:customer_id>/posts/<int:post_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(post_params))
@require_id_exists()
@require_ownership(customer_id=['post_id'])
def post(customer_id, post_id):
	if request.method == PUT:
		edit_obj(Post, post_id, request.json)
		return jsonify(message="Successfully edited post"), 200
	if request.method == DELETE:
		delete_obj(Post, post_id)
		return jsonify(message="Successfully deleted post"), 200


@app.route("customers/<int:customer_id>/posts/<int:post_id>/likes", methods=[GET])
@require_id_exists()
def post_likes(customer_id, post_id):
	if request.method == GET:
		return jsonify([post_.likes for post_ in find(Post, post_id)]), 200


@app.route("/customers/<int:customer_id>/likes", methods=[GET, POST])
@require_method_params(POST=["post_id"])
@require_id_exists()
def likes(customer_id):
	if request.method == GET:
		return jsonify([customer.likes for customer in find(Customer, customer_id)]), 200
	if request.method == POST:
		create_obj(Like, request.json | dict(user_id=customer_id))
		return jsonify(message="Successfully liked post"), 201


@app.route("/customers/<int:customer_id>/likes/<int:like_id>", methods=[DELETE])
@require_method_params()
@require_ownership(customer_id=['like_id'])
def like(customer_id, like_id):
	if request.method:




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
