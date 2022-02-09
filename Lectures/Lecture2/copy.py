from flask import Flask
from flask import jsonify
from flask import request
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///./our.db'
db = SQLAlchemy(app)


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(60), unique=True)
    emails = db.relationship('Email', backref='person', lazy=True)

    def to_dict(self):
        result = {}
        result['name'] = self.name
        result['id'] = self.id
        result['emails'] = [x.address for x in self.emails]
        return result


class Email(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    address = db.Column(db.String(100), unique=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

emails = {}

@app.route("/")
def hello_world():
    return "Hello world "


@app.route("/hello/<user>/from/<country>")
def hello(user, country):
    return "This is hello!!!" + str(user) + " it seems your are from " + str(country)


@app.route("/add/<name>/<email>", methods=['GET'])
def add_email_to_user(name, email):
    if name not in emails:
        emails[name] = []
    emails[name].append(email)
    return "", 200


@app.route("/add_with_post", methods=["POST"])
def add_with_post():
    data = request.json
    print(data)
    user = data['user']
    email = data['email']
    u = User.query.filter_by(name=user).first()
    if u is None:
        u = User(name=user)
    temp_email = Email.query.filter_by(address=email)
    if temp_email is None:
        u.emails.append(Email(address=email))
        db.session.add(u)
        db.session.commit()
        return "", 200
    return '{response:"Emails alrady in db"}', 400


@app.route("/get/<user>")
def get_emails(user):
    u = User.query.filter_by(name=user).first()
    if u is not None:
        return jsonify(u.to_dict()), 200
    return '{response:"No such user"}', 404


if __name__ == "__main__":
    app.debug = True
    app.run(port=5080)
