import os
from datetime import timedelta

ACCESS_EXPIRES = timedelta(hours=1)

if 'DATABASE_URL_TRUE' in os.environ:
    db_uri = os.environ['DATABASE_URL_TRUE']
    debug_flag = False
else: # when running locally: use sqlite
    db_path = os.path.join(os.path.dirname(__file__), 'our.db')
    db_uri = 'sqlite:///{}'.format(db_path)
    debug_flag = True

SQLALCHEMY_DATABASE_URI = db_uri
SQLALCHEMY_TRACK_MODIFICATIONS = False
# UPLOAD_FOLDER = '/path/to/the/uploads'
UPLOAD_FOLDER = '/uploads'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}
JWT_SECRET_KEY = 'dsyr'
JWT_ACCESS_TOKEN_EXPIRES = ACCESS_EXPIRES
