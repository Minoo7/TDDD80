from flask_marshmallow import sqla as ma_sqla
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import sessionmaker

ValidationError = ma_sqla.ValidationError


from flask import Flask

app = Flask(__name__)
app.config.from_pyfile('config.py')
