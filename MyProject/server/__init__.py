import os

from flask_marshmallow import sqla as ma_sqla
from flask_sqlalchemy import SQLAlchemy

ValidationError = ma_sqla.ValidationError
MYDIR = os.path.dirname(__file__)


from flask import Flask

app = Flask(__name__)
app.config.from_pyfile('config.py')

# from flask_sqlalchemy import SQLAlchemy
# from sqlalchemy import Column, Integer

# from MyProject.server import app

db = SQLAlchemy(app)
Model = db.Model
session = db.session
