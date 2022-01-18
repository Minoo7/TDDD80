import uuid

from flask import Flask, jsonify, request

app = Flask(__name__)
database = {}
messages = set()


@app.route("/")
def home():
    return "Hello! this is the main page <h1>HELLO</h1>"


@app.route("/<name>")
def user(name):
    return f"Hello {name}!"


@app.route("/save/<msg>")
def save(msg):
    if msg not in messages:
        messages.add(msg)
    return "", 200


@app.route("/show/")
def show_msgs():
    lst = []
    for msg in messages:
        lst.append(msg)
    return jsonify(lst)
    return "", 200


@app.route("/login", methods=["POST", "GET"])
def login():
    print(request)
    return "", 200


@app.route("/messages", methods=["POST"])
def messages():
    msg = request.json['message']
    generated_id = str(uuid.uuid4())
    print(f"generated_id: {generated_id}")
    database[generated_id] = {'id': generated_id, 'message': msg, 'readBy': []}
    database['test'] = {'id': '123'}
    #print({'id': generated_id})
    return {'id': generated_id}, 200 #?*


@app.route("/messages/<MessageID>", methods=["GET"])
def get_msg(MessageID):
    print(f"ret: {database[MessageID]}")
    return database[MessageID], 200

if __name__ == "__main__":
    app.debug = True
    app.run(port=5087)
