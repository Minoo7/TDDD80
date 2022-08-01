import ast
from datetime import datetime, timezone
import boto3 as boto3
import werkzeug
from flask import request, jsonify, abort
from flask_bcrypt import check_password_hash
from flask_jwt_extended import create_access_token, JWTManager, jwt_required, get_jwt_identity, get_jwt
from werkzeug.exceptions import HTTPException
from werkzeug.utils import secure_filename

from MyProject.server import app, ValidationError, groups, session, s3
from MyProject.server.config import ALLOWED_EXTENSIONS, AWS_STORAGE_BUCKET_NAME
from MyProject.server.constants import GET, POST, DELETE, PUT
from MyProject.server.main import edit_obj, find, create_obj, delete_obj, \
	get_obj, get_all, find_possible_logins, find_by
from MyProject.server.models import Customer, Address, Post, ImageReference, Like, Comment, TokenBlocklist
from MyProject.server.routes.helpers import require_method_params, require_id_exists, customer_required_params, \
	address_params, \
	post_params, require_ownership, comment_params, require_json_id_exists, customer_params
from MyProject.server.validation.validate import IdError

jwt = JWTManager(app)


@app.route("/", methods=["GET"])
def home():
	return "Hello World", 200


def allowed_file(filename):
	return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@jwt.token_in_blocklist_loader
def check_if_token_revoked(jwt_header, jwt_payload):
	jti = jwt_payload['jti']
	token = session.query(TokenBlocklist.id).filter_by(jti=jti).scalar()
	return token is not None


@app.route("/login", methods=[POST])
@require_method_params(POST=["login_method_name", "password"])
def login():
	if request.method == POST:
		data = request.get_json()
		login_method_name = data.get("login_method_name")
		password = data.get("password")

		existing_customer = None

		for customer in find_possible_logins(login_method_name):
			if check_password_hash(customer.password, password):
				existing_customer = customer
		if not existing_customer:
			abort(400, "Wrong username or password")

		# checking for first login by checking if an address has been added:
		first_login = existing_customer.address is None

		return jsonify(message="Successfully logged in", id=existing_customer.id,
					   token=create_access_token(identity=existing_customer.id), first_login=first_login), 200


@app.route("/logout", methods=["POST"])
@jwt_required()
def modify_token():
	jti = get_jwt()['jti']
	now = datetime.now(timezone.utc)
	session.add(TokenBlocklist(jti=jti, created_at=now))
	session.commit()
	return jsonify(message="You are out"), 200


@app.route("/customers", methods=[GET, POST])
@require_method_params(POST=customer_required_params)
def customers():
	if request.method == GET:
		return jsonify(get_all(Customer)), 200
	if request.method == POST:
		_customer = create_obj(Customer, request.json)
		return jsonify(message="Successfully created customer", customer_number=_customer.customer_number), 201


@app.route("/customers/mini", methods=[GET])
def customers_mini():
	if request.method == GET:
		mini_customers = [customer.get_mini() for customer in Customer.query.all()]
		return jsonify(mini_customers), 200


@app.route("/customers/<int:customer_id>", methods=[GET, PUT, DELETE])
@require_method_params(PUT=set(customer_params))
@jwt_required()
@require_ownership()
def update_customers(customer_id):
	if request.method == GET:
		return get_obj(Customer, customer_id), 200
	if request.method == PUT:
		edit_obj(Customer, customer_id, request.json)
		return jsonify(message="Successfully edited Customer"), 200
	if request.method == DELETE:
		delete_obj(Customer, customer_id)
		return jsonify(message="Successfully deleted Customer"), 200


@app.route("/customers/<int:customer_id>/mini", methods=[GET])
def as_mini(customer_id):
	if request.method == GET:
		return jsonify(find(Customer, customer_id).get_mini()), 200


@app.route("/customers/<int:customer_id>/addresses", methods=[GET, POST])
@require_method_params(POST=address_params)
@jwt_required()
@require_ownership()
def addresses(customer_id):
	if request.method == GET:
		customer_address = find(Customer, customer_id).address
		return jsonify(customer_address.street), 200
	if request.method == POST:
		if find(Customer, customer_id).has_address(groups.AddressTypes[request.json["address_type"]]):
			return jsonify(message="Customer already has address with this type"), 400
		create_obj(Address, request.json | dict(customer_id=customer_id))
		return jsonify(message="Successfully created address"), 201


@app.route("/customers/<int:customer_id>/addresses/<int:address_id>", methods=[PUT, DELETE])
@require_method_params(PUT=set(address_params))
@jwt_required()
@require_ownership(customer_id=['address_id'])
def address(customer_id, address_id):
	if request.method == PUT:
		edit_obj(Address, address_id, request.json)
		return jsonify(message="Successfully edited address"), 200
	if request.method == DELETE:
		delete_obj(Address, address_id)
		return jsonify(message="Successfully deleted address"), 200


@app.route("/customers/<int:customer_id>/images", methods=[POST])
@jwt_required()
@require_ownership()
def images(customer_id):
	if request.method == POST:
		if 'file' not in request.files:
			return jsonify(message='No file part in the request'), 400

		file = request.files['file']
		if not file.filename:
			return jsonify(message='No file selected for uploading'), 400

		if file and allowed_file(file.filename):
			filename = secure_filename(file.filename)

			s3_resource = boto3.resource('s3')
			my_bucket = s3_resource.Bucket(AWS_STORAGE_BUCKET_NAME)
			my_bucket.Object(filename).put(Body=file)

			img = create_obj(ImageReference, {'path': filename})
			return jsonify(message='File successfully uploaded', id=img.id), 201
		else:
			return jsonify(message='Allowed file types are png, jpg, jpeg'), 400


@app.route("/customers/<int:customer_id>/images/<int:image_id>", methods=[DELETE])
@jwt_required()
@require_ownership(customer_id=['image_id'])
def image(customer_id, image_id):
	if request.method == DELETE:
		delete_obj(ImageReference, image_id)
		return jsonify(message="Successfully deleted image"), 200


# https://www.moesif.com/blog/technical/api-design/REST-API-Design-Best-Practices-for-Sub-and-Nested-Resources/
# Using security approach for images - not using user_id as parameter for images
# write that using mvvm in android
# or ?


@app.route("/customers/<int:customer_id>/following", methods=[POST])
@require_method_params(POST=["customer_id"])
@jwt_required()
@require_ownership()
@require_json_id_exists("customer_id")
def follow(customer_id):
	follow_id = request.json["customer_id"]
	find(Customer, customer_id).follow(find(Customer, follow_id))
	session.commit()
	return jsonify(message=f"Successfully followed user: ({follow_id})"), 201


@app.route("/customers/<int:customer_id>/following/<int:follow_id>", methods=[DELETE])
@jwt_required()
@require_ownership()
@require_id_exists()
def unfollow(customer_id, follow_id):
	if request.method == DELETE:
		Customer.query.get(customer_id).unfollow(Customer.query.get(follow_id))
		# find(Customer, customer_id).unfollow(find(Customer, follow_id))
		session.commit()
		return jsonify(message=f"Successfully unfollowed user: ({follow_id})"), 200

"""@app.route("/customers/<int:customer_id_to_unfollow>/following", methods=[DELETE])
@jwt_required()
@require_id_exists()
def unfollow(customer_id_to_unfollow):
	if request.method == DELETE:
		Customer.query.get(get_jwt_identity()).unfollow(Customer.query.get(customer_id_to_unfollow))
		session.commit()
		return jsonify(message=f"Successfully unfollowed user: ({customer_id_to_unfollow})"), 200"""


@app.route("/customers/<int:customer_id>/feed", methods=[GET])
# @jwt_required()
# @require_ownership()
def feed(customer_id):
	if request.method == GET:
		return jsonify(find(Customer, customer_id).get_feed()), 200


@app.route("/customers/<int:customer_id>/profile", methods=[GET])
@require_id_exists()
def profile(customer_id):
	if request.method == GET:
		return jsonify(find(Customer, customer_id).get_profile()), 200


@app.route("/posts", methods=[GET, POST])
@require_method_params(POST=post_params)
@jwt_required()
def posts():
	if request.method == GET:
		return jsonify(get_all(Post)), 200
	if request.method == POST:
		create_obj(Post, request.json | dict(customer_id=get_jwt_identity()))  # get_jwt_identity()
		return jsonify(message="Successfully created post"), 201


@app.route("/posts/<int:post_id>", methods=[DELETE])
@jwt_required()
@require_ownership(customer_id=['post_id'])
def post(post_id):
	if request.method == DELETE:
		delete_obj(Post, post_id)
		return jsonify(message="Successfully deleted post"), 200


@app.route("/posts/<int:post_id>/likes", methods=[GET, POST])
@jwt_required()
def likes(post_id):
	if request.method == GET:
		return jsonify([post_.likes for post_ in find(Post, post_id)]), 200
	if request.method == POST:
		json = dict(post_id=post_id, customer_id=get_jwt_identity())
		if find_by(Like, **json):
			return jsonify(message="Already liked post"), 200
		create_obj(Like, json)
		return jsonify(message="Successfully liked post"), 201


"""@app.route("/posts/<int:post_id>/likes/<int:like_id>", methods=[DELETE])
@jwt_required()
@require_ownership(customer_id=['like_id'], post_id=['like_id'])
def like(post_id, like_id):
	if request.method == DELETE:
		delete_obj(Like, like_id)
		return jsonify(message="Successfully deleted like"), 200"""


@app.route("/posts/<int:post_id>/unlike", methods=[DELETE])
@jwt_required()
def unlike(post_id):
	if request.method == DELETE:
		json = dict(post_id=post_id, customer_id=get_jwt_identity())
		like_id = find_by(Like, **json).id
		delete_obj(Like, like_id)
		return jsonify(message="Successfully deleted like"), 200


@app.route("/posts/<int:post_id>/comments", methods=[GET, POST])
@require_method_params(POST=comment_params)
@jwt_required()
def comments(post_id):
	if request.method == GET:
		return jsonify([post_.comments for post_ in find(Post, post_id)]), 200
	if request.method == POST:
		create_obj(Comment, request.json | dict(post_id=post_id, customer_id=get_jwt_identity()))
		return jsonify(message="Successfully commented on post"), 201


@app.route("/posts/<int:post_id>/comments/<int:comment_id>", methods=[DELETE])
@jwt_required()
@require_ownership(customer_id=['comment_id'], post_id=['comment_id'])
def comment(post_id, comment_id):
	if request.method == DELETE:
		delete_obj(Comment, comment_id)
		return jsonify(message="Successfully deleted comment"), 200


@app.route("/customers/<int:customer_id>/likes", methods=[GET])
@require_method_params(POST=["post_id"])
@jwt_required()
@require_ownership()
def customer_likes(customer_id):
	if request.method == GET:
		return jsonify([customer.likes for customer in find(Customer, customer_id)]), 200


@app.errorhandler(IdError)
def handle_bad_id(e):
	return jsonify(str(e)), 400


@app.errorhandler(ValidationError)
def handle_bad_validation(e):
	return jsonify(ast.literal_eval(str(e))), 400


@app.errorhandler(werkzeug.exceptions.BadRequest)
def handle_bad_request(e):
	return jsonify(str(e).split(':')[1][1:]), 400


@app.errorhandler(404)
def resource_not_found(e):
	return jsonify(str(e)), 404


@app.errorhandler(ValueError)
def handle_value_error(e):
	return jsonify(str(e)), 404
