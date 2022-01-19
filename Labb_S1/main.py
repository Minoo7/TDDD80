import uuid

from flask import Flask, jsonify, request

app = Flask(__name__)
database = {}


# @app.route("/")
# def home():
#    return "Hello! this is the main page <h1>HELLO</h1>"


@app.route("/messages", methods=["GET", "POST"])
def messages():
    if request.method == "POST":
        msg = request.json['message']
        generated_id = str(uuid.uuid4())
        database[generated_id] = {'id': generated_id, 'message': msg, 'readBy': []}
        return jsonify({'id': generated_id}), 200
    else:  # method is GET
        return jsonify(database), 200


@app.route("/messages/<MessageID>", methods=["GET"])
def get_msg(MessageID):
    return jsonify(database[MessageID]), 200


@app.route("/messages/<MessageID>", methods=["DELETE"])
def delete_msg(MessageID):
    database.pop(MessageID, None)
    return "", 200


@app.route("/messages/<MessageID>/read/<UserId>", methods=["POST"])
def read_msg(MessageID, UserId):
    if MessageID in database:
        database[MessageID]['readBy'].append(UserId)
    return "", 200


@app.route("/messages/unread/<UserId>", methods=["GET"])
def get_unread_msg(UserId):
    new_list = []
    for key in database:
        if UserId not in database[key]['readBy']:
            new_list.append(database[key])
    return jsonify(new_list), 200


if __name__ == "__main__":
    app.debug = True
    app.run(port=5087)
