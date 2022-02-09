from flask import Flask, jsonify, request, Response
from flask_sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
db_uri = 'sqlite:///./our.db'
if 'DATABASE_URL_TRUE' in os.environ:
    db_uri = os.environ['DATABASE_URL_TRUE']
app.config['SQLALCHEMY_DATABASE_URI'] = db_uri
db = SQLAlchemy(app)

#read_messages = db.Table('read_messages', db.Model.metadata,
#                         db.Column('messages_id', db.Integer, db.ForeignKey('message.id')),
#                         db.Column('user_id', db.Integer, db.ForeignKey('user.id')))


class User(db.Model):  # User abstract class
    __abstract__ = True
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(32), nullable=False)
    password = db.Column(db.String(32), nullable=False)
    first_name = db.Column(db.String(32))
    last_name = db.Column(db.String(32))
    email = db.Column(db.String(150), nullable=False)
    gender = db.Column(db.Enum('test', name='genders'))
    # Relations:
    # rel = db.relationship("Message", secondary=read_messages, backref="readBy", lazy=True)

    def to_dict(self):
        return {'id': self.id, 'Name': self.name, 'read': [read.message for read in self.messages_read]}


class Administrator(User):
    permission_group = db.Column(db.Enum('group1', name='permission_groups'), nullable=False)


class Customer(User):
    # id = db.Column(db.Integer, primary_key=True)
    customer_number = db.Column(db.String(32), nullable=False)
    phone_number = db.Column(db.String(20), nullable=False)
    business_type = db.Column(db.Enum('restaurant', name='business_types'))
    organization_number = db.Column(db.String(11), nullable=False)

    # Relations:
    # address_id
    # business_address_id


class Address(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    address_type = db.Column(db.Enum('home', name='address_types'), nullable=False)  # Home, Billing, Both
    street = db.Column(db.String(95), nullable=False)
    city = db.Column(db.String(35), nullable=False)
    zip_code = db.Column(db.String(11), nullable=False)

    # Optional:
    apartment_number = db.Column(db.String(20))
    other_info = db.Column(db.String(255))

    # Relation:
    # customer_id


class Post(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    # FK customer_id

    # Either content or image needs to exist:
    content = db.Column(db.String(255))
    # Relation image_id = db.Column(db.Integer)

    type = db.Column(db.Enum("front", name='post_types'), nullable=False)

    created_at = db.Column(db.DateTime(), nullable=False)
    updated_at = db.Column(db.DateTime())

    # total likes
    # total comments


class Feed(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    # customer_id #FK
    # post_id #FK (list: multiple)


class Follower(db.Model):
    id = db.Column(db.Integer, primary_key=True)

    # FK customer_id
    # FK customer_follower_id


class Comment(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    # FK post_id
    # FK customer_id
    created_at = db.Column(db.DateTime(), nullable=False)


class Like(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    # FK post_id
    # FK user_id


# class Image()


class Message(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    message = db.Column(db.String(140))

    def to_dict(self):
        return {'id': self.id, 'message': self.message, 'readBy': [f"{usr.name} ({usr.id})" for usr in self.readBy]}


@app.route("/")
def hello_world():
    return "This i hello"


@app.route("/messages", methods=["GET", "POST"])
def messages():
    if request.method == "POST":
        received = request.json
        if received is not None:
            msg = Message(message=received['message'])
            db.session.add(msg)
            db.session.commit()
            return jsonify({'id': msg.id}), 200
        return jsonify({"response": "No message provided"}), 404
    elif request.method == "GET":
        return jsonify([msg.to_dict() for msg in Message.query.all()]), 200


@app.route("/messages/<message_id>", methods=["GET", "DELETE"])
def get_msg(message_id):
    if request.method == "GET":
        message = Message.query.filter_by(id=int(message_id)).first()
        if message is not None:
            return jsonify(message.to_dict()), 200
        return 'Message id not found in database', 404
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
    return "", 200


reset()
if __name__ == "__main__":
    reset()
    app.debug = True
    app.run(port=5080)
