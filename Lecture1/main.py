# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

from flask import Flask
from flask import jsonify
from flask import request

app = Flask(__name__)
emails = {}


@app.route("/")
def hello_world():
    return "This i hello"


@app.route("/hello/<user>/from/<country>")
def hello(user, country):
    return "This is hello!!!" + str(user) + " it seems you are from " + str(country)


@app.route("/add/<name>/<email>", methods=['GET'])
def add_email_to_user(name, email):
    if name not in emails:
        emails[name] = []
    emails[name].append(email)
    return "", 200


@app.route("/add_with_post", methods=['POST'])
def add_with_post():
    data = request.json
    user = data['user']
    email = data['email']
    if user not in emails:  # bör göras med funktion och ej upprepas
        emails[user] = []
    emails[user].append(email)
    return "", 200


@app.route("/get/<user>")
def get_emails(user):
    if user in emails:
        return jsonify(emails[user]), 200
    else:
        return '{response:"No such user"}', 404


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    # print_hi('PyCharm')
    app.debug = True
    app.run(port=5080)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
