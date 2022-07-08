import os

import boto3 as boto3
from flask_marshmallow import sqla as ma_sqla
from flask_sqlalchemy import SQLAlchemy
from botocore.client import Config

from MyProject.server.config import AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY

ValidationError = ma_sqla.ValidationError
MYDIR = os.path.dirname(__file__)

from flask import Flask

app = Flask(__name__)
app.config.from_pyfile('config.py')

db = SQLAlchemy(app)
Model = db.Model
session = db.session

s3 = boto3.client('s3', aws_access_key_id=AWS_ACCESS_KEY_ID, aws_secret_access_key=AWS_SECRET_ACCESS_KEY, region_name='eu-north-1', config=Config(signature_version='s3v4'))
