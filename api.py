from flask import request, jsonify, Response, redirect, render_template

from server import app, db
from main import *
from constants import *
from flask_wtf import FlaskForm
from wtforms import StringField, Form, EmailField, validators
from wtforms.validators import DataRequired


@app.route("/")
def hello_world():
    return "This i hello"


@app.route("/add/customer", methods=["POST"])
def add_customer():
    if request.method == "POST":
        received = request.json
        if received is not None:
            try:
                username    = received['username']
                password    = received['password']
                first_name  = received['first_name']
                last_name   = received['']
                #main.add_customer()
            except InputNotValidError:
                pass


"""@app.route("/messages", methods=["GET", "POST"])
def messages():
    if request.method == "POST":
        received = request.json
        if received is not None:
            return jsonify(add_message(received['message'], "Sender")), OK
        return jsonify({"response": "No message provided"}), NOT_FOUND
    elif request.method == "GET":
        return jsonify(get_messages()), OK


@app.route("/messages/<message_id>", methods=["GET", "DELETE"])
def get_msg(message_id):
    if request.method == "GET":
        message = Message.query.filter_by(id=int(message_id)).first()
        if message is not None:
            return jsonify(message.to_dict()), OK
        return 'Message id not found in database', NOT_FOUND
    elif request.method == "DELETE":
        message = Message.query.filter_by(id=int(message_id)).first()
        if message is not None:
            db.session.delete(message)  # .commit()
            db.session.commit()
            return "", 200
        return 'Message id not found in database', 404


@app.route("/user/<name>", methods=["POST"])
def add_user(name):
    if request.method == "POST":
        user = User(name=name)
        db.session.add(user)
        db.session.commit()
        return jsonify({'id': user.id}), 200
    return "Wrong method", 404


@app.route("/messages/<message_id>/read/<user_id>", methods=["POST"])
def read_msg(message_id, user_id):
    message = Message.query.filter_by(id=int(message_id)).first()
    if message is None:
        return jsonify({'response': 'Message id not found in database'}), 404

    user = User.query.filter_by(id=int(user_id)).first()
    if user is None:
        return jsonify({'response': 'User id does not exist'}), 404

    message.readBy.append(user)
    db.session.commit()
    return "", 200


@app.route("/messages/unread/<user_id>", methods=["GET"])
def get_unread_msg(user_id):
    if not User.query.filter_by(id=int(user_id)).first():
        return jsonify({'response': 'User id does not exist'}), 404
    return jsonify([msg.to_dict() for msg in Message.query.filter(~Message.readBy.any(id=user_id))]), 200


@app.route("/reset")
def reset():
    db.drop_all()
    db.create_all()
    return "", 200"""
