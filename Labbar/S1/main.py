import uuid

from flask import Flask, jsonify, request

app = Flask(__name__)
database = []


# @app.route("/")
# def home():
#    return "Hello! this is the main page <h1>HELLO</h1>"


def find_msg(MessageID):
    for json_msg in database:
        if json_msg['id'] == MessageID:
            return database.index(json_msg)
    return False


@app.route("/messages", methods=["GET", "POST"])
def messages():
    if request.method == "POST":
        msg = request.json['message']
        if len(msg) > 140:
            return "Message can not be larger than 140 characters", 400
        generated_id = str(uuid.uuid4())
        id_dict = {'id': generated_id}
        database.append({'id': id_dict['id'], 'message': msg, 'readBy': []})
        return jsonify(id_dict), 200
    else:  # method is GET
        return jsonify(database), 200


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


if __name__ == "__main__":
    app.debug = True
    app.run(port=5087)
