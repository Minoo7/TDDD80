import uuid
from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///./our.db'
db = SQLAlchemy(app)
database = []

#assocation table with message_id and user_id
#left table (user) lists all users
#right table lists all messages

emails = {}


#class Email(db.Model):
#    id = db.Column(db.Integer, primary_key=True)
#    address = db.Column(db.String(100), unique=True)
#    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)


def find_msg(MessageID):
    for json_msg in database:
        if json_msg['id'] == MessageID:
            return database.index(json_msg)
    return False


class User(db.Model):
    name = db.Column(db.String(60))
    id = db.Column(db.String(90), primary_key=True)
    messages = db.relationship("Message", backref='person', lazy=True)
    #emails = db.relationship('Email', backref='person', lazy=True)

    def to_dict(self):
        result = {}
        result['emails'] = [x.message for x in self.message_id]
        return result


class Message(db.Model):
    #__tablename__ = 'messages'
    #id = db.Column(db.Integer, primary_key=True)
    id = db.Column(db.String(90), primary_key=True)
    message = db.Column(db.String(140), nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)


class Email(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    address = db.Column(db.String(100), unique=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

@app.route("/add", methods=["POST"])
def add():
    print("yo")
    data = request.json
    print(data["name"])
    if not User.query.filter_by(name=data['name']).first():
        user = User(name=data['name'])
        db.session.add(user)
        db.session.commit()
    return "", 200


@app.route("/messages", methods=["GET", "POST"])
def messages():
    if request.method == "POST":
        data = request.json
        msg = data["message"]
        user = data["user"]
        db.drop_all()
        db.create_all()
        #id_dict = {'id': generated_id}
        usr = User.query.filter_by(name=user).first()
        #print(usr)
        if usr is None:
            usr = User(name=user)
        #print(usr.messages)
        #temp = Message.query.filter_by(message="hello thisq isaa a message").first()
        #print(temp)
        temp = None
        if temp is None: # Does not already exist?
            #usr.emails.append(Email(address="email@liu.se"))
            usr.messages.append(Message(message="email@liu.se"))
            #usr.message_id.append(Message.query.filter_by(message="hello thisq isaa a message").first())
            db.session.add(usr)
            #print(usr.messages)
            db.session.commit()
            return jsonify({"response": "yep"}), 200
        return jsonify({'response': 'Emails alrady in db'}), 400
        #database.append(id_dict | {'message': msg, 'readBy': []})
        #return jsonify(id_dict), 200
    elif request.method == "GET":  # method is GET
        #print("hej")
        #return jsonify({"h": "k"}), 200
        usr = User.query.filter_by(name="vins").first()
        if usr is not None:
            return jsonify(usr.to_dict()), 200
        return jsonify(usr.to_dict()), 200
        #return jsonify(usr.to_dict()), 200


@app.route("/messages/<MessageID>", methods=["GET", "DELETE"])
def get_msg(MessageID):
    if request.method == "GET":
        found = find_msg(MessageID)
        if found is not False:
            return jsonify(database[found]), 200
        return "MessageID not found in database", 404
    elif request.method == "DELETE":
        found = find_msg(MessageID)
        if found is not False:
            database.pop(found)
            return "", 200
        return "MessageID not found in database", 404


@app.route("/messages/<MessageID>/read/<UserId>", methods=["POST"])
def read_msg(MessageID, UserId):
    found = find_msg(MessageID)
    if found is not False:
        database[find_msg(MessageID)]['readBy'].append(UserId)
        return "", 200
    return "MessageID not found in database", 404


@app.route("/messages/unread/<UserId>", methods=["GET"])
def get_unread_msg(UserId):
    new_list = []
    for json in database:
        if UserId not in json['readBy']:
            new_list.append(json)
    return jsonify(new_list), 200


@app.route("/reset")
def reset():
    db.drop_all()
    db.create_all()
    return "", 200


if __name__ == "__main__":
    db.drop_all()
    db.create_all()
    app.debug = True
    app.run(port=5080)
