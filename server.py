from flask import Flask, jsonify, request, Response
from flask_sqlalchemy import SQLAlchemy, declarative_base
from sqlalchemy.orm import relationship, sessionmaker
from flask_marshmallow import Marshmallow, sqla as ma_sqla

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app=app)
Session = sessionmaker(bind=db.get_engine(), autoflush=True)
db.session = Session()
ma = Marshmallow(app)

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
