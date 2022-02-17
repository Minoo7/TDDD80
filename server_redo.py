from datetime import datetime, timedelta, timezone

import flask_jwt_extended
import psycopg2
from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_jwt_extended import create_access_token, JWTManager, jwt_required, get_jwt_identity, unset_jwt_cookies, \
	get_raw_jwt
import os

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)
bcrypt = Bcrypt(app)
jwt = JWTManager(app)

read_messages = db.Table('read_messages', db.Model.metadata,
						 db.Column('messages_id', db.Integer, db.ForeignKey('message.id')),
						 db.Column('user_id', db.Integer, db.ForeignKey('user.id')))


class User(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.String(60), unique=True, nullable=False)
	password = db.Column(db.String(200), unique=False, nullable=False)
	messages_read = db.relationship("Message", secondary=read_messages, backref="readBy", lazy=True)

	def __init__(self, name, password):
		self.name = name
		self.password = bcrypt.generate_password_hash(password)

	def to_dict(self):
		return {'id': self.id, 'Name': self.name, 'read': [read.message for read in self.messages_read]}


class TokenBlocklist(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	jti = db.Column(db.String(36), nullable=False)
	created_at = db.Column(db.DateTime, nullable=False)


@jwt.token_in_blacklist_loader
def check_if_token_revoked(jwt_header, jwt_payload):
	jti = jwt_payload['jti']
	token = db.session.query(TokenBlocklist.id).filter_by(jti=jti).scalar()
	return token is not None


@app.route("/logout", methods=["POST"])
@jwt_required
def modify_token():
	jti = get_raw_jwt()['jti']  # get_jwt deprecated
	now = datetime.now(timezone.utc)
	db.session.add(TokenBlocklist(jti=jti, created_at=now))
	db.session.commit()
	return jsonify(message="You are out"), 200


class Message(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	message = db.Column(db.String(140))

	def to_dict(self):
		return {'id': self.id, 'message': self.message, 'readBy': [f"{usr.name} ({usr.id})" for usr in self.readBy]}


@app.route("/")
def home():
	return "Hello World"


@app.route("/messages", methods=["GET"])
def get_messages():
	if request.method == "GET":
		return jsonify([msg.to_dict() for msg in Message.query.all()]), 200
	return "Wrong method", 404


@app.route("/messages", methods=["POST"])
@jwt_required
def messages():
	if request.method == "POST":
		received = request.json
		if received is not None:
			msg = Message(message=received['message'])
			db.session.add(msg)
			db.session.commit()
			return jsonify({'id': msg.id}), 200
		return jsonify({"response": "No message provided"}), 404
	else:
		return "Wrong method", 404


@app.route("/messages/<message_id>", methods=["GET"])
def get_msg(message_id):
	if request.method == "GET":
		message = Message.query.filter_by(id=int(message_id)).first()
		if message is not None:
			return jsonify(message.to_dict()), 200
		return 'Message id not found in database', 404
	return "Wrong method", 404


@app.route("/messages/<message_id>", methods=["DELETE"])
@jwt_required
def delete_msg(message_id):
	if request.method == "DELETE":
		message = Message.query.filter_by(id=int(message_id)).first()
		if message is not None:
			db.session.delete(message)  # .commit()
			db.session.commit()
			return "", 200
		return 'Message id not found in database', 404
	return "Wrong method", 404


@app.route("/user", methods=["POST"])
def add_user():
	if request.method == "POST":
		received = request.json
		if received is not None:
			name = received['name']
			password = received['password']
			if name is None or password is None:
				return jsonify({"response": "name or password missing"}), 404
			existing_user = User.query.filter_by(name=name).first()
			if existing_user is None:
				user = User(name=name, password=password)
				db.session.add(user)
				db.session.commit()
				return jsonify({'id': user.id}), 200
			return jsonify({"response": "User already exists"}), 404
		else:
			return jsonify({"response": "No "}), 404
	return "Wrong method", 404


@app.route("/user/login", methods=["POST"])
def login():
	if request.method == "POST":
		received = request.json
		if received is not None:
			name = received['name']
			password = received['password']
			if name is None or password is None:
				return jsonify({"response": "name or password missing"}), 404
			existing_user = User.query.filter_by(name=name).first()
			if existing_user is None:
				return jsonify(response="Wrong username or password"), 400
			if bcrypt.check_password_hash(existing_user.password, password):
				return jsonify({'token': create_access_token(identity=existing_user.name)}), 200
			return jsonify(response="Wrong username or password"), 400
	return "Wrong method", 404


@app.route("/messages/<message_id>/read/<user_id>", methods=["POST"])
@jwt_required
def read_msg(message_id, user_id):
	message = Message.query.filter_by(id=int(message_id)).first()
	if message is None:
		return jsonify({'response': 'Message id not found in database'}), 404

	# user = User.query.filter_by(id=int(user_id)).first()
	username = get_jwt_identity()
	user = User.query.filter_by(name=username).first()
	if user is None:
		return jsonify({'response': 'User id does not exist'}), 404

	message.readBy.append(user)
	db.session.commit()
	return "", 200


@app.route("/messages/unread/<user_id>", methods=["GET"])
@jwt_required
def get_unread_msg(user_id):
	if not User.query.filter_by(name=get_jwt_identity()).first():
		return jsonify({'response': 'User id does not exist'}), 404
	return jsonify([msg.to_dict() for msg in Message.query.filter(~Message.readBy.any(id=user_id))]), 200


def init_db():
	try:
		db.drop_all()
	except psycopg2.errors.UndefinedTable as ut:
		print("Table does not exist")
	db.create_all()
	meta = db.metadata
	for table in reversed(meta.sorted_tables):
		db.session.execute(table.delete())
	db.session.close()


@app.route("/reset")
def reset():
	db.drop_all()
	return "", 200


# reset()
if __name__ == "__main__":
	init_db()
	app.debug = True
	app.run(port=5080)
