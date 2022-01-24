from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///./our.db'
db = SQLAlchemy(app)


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(60), unique=True)
    emails = db.relationship('Email', backref='person', lazy=True)

    def to_dict(self):
        return {'name': self.name, 'id': self.id, 'emails': [email.address for email in self.emails]}


class Email(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    address = db.Column(db.String(100), unique=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)


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
    return jsonify({'response': 'Email is already in db response'}), 400


@app.route("/get/<user>")
def get_emails(user):
    u = User.query.filter_by(name=user).first()
    if u is not None:
        return jsonify(u.to_dict()), 200
    return jsonify({'response': 'No such user'}), 404

"""
server.db.create_all()
u = server.User(name='Anders')
$ u
$ u.name
$ u.emails
e = server.Email(address='anders.froberg')
$ e
u.emails.append(e)
server.db.session.commit()
$ e.address
$ e.person
//
server.db.drop_all()
"""

"""
server.db.create_all()
"""

if __name__ == '__main__':
    # print_hi('PyCharm')
    app.debug = True
    app.run(port=5080)