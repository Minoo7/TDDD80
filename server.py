from flask import Flask, jsonify, request, Response
from flask_marshmallow import Marshmallow, sqla as ma_sqla
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config.from_pyfile('config.py')
db = SQLAlchemy(app)
ma = Marshmallow(app)
import models


def create_app():
    ma.init_app(app)
    db.init_app(app)
    Session = sessionmaker(bind=db.get_engine(app=app), autoflush=True)
    db.session = Session()
    return app

from models import *


counter = 7


def reset_db():
    db.drop_all()
    db.create_all()
    return "", 200

if __name__ == "__main__":
    with app.app_context():
        create_app()
        reset_db()
        app.debug = True
        app.run(port=5080)
