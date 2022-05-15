import os

from flask_marshmallow import sqla as ma_sqla
from flask_sqlalchemy import SQLAlchemy

ValidationError = ma_sqla.ValidationError
MYDIR = os.path.dirname(__file__)

from flask import Flask

app = Flask(__name__)
app.config.from_pyfile('config.py')

db = SQLAlchemy(app)
Model = db.Model
session = db.session
