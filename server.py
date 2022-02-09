from flask import Flask, jsonify, request, Response
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)

# from api import *
from models import *


def reset_db():
    db.drop_all()
    db.create_all()
    return "", 200


if __name__ == "__main__":
    reset_db()
    app.debug = True
    app.run(port=5080)
