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


@app.route("/login", methods=["POST", "GET"])
def login():
    return "d"



if __name__ == "__main__":
    app.debug = True
    app.run(port=5080)
