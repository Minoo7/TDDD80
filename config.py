import os

if 'DATABASE_URL_TRUE' in os.environ:
    db_uri = os.environ['DATABASE_URL_TRUE']
    debug_flag = False
else: # when running locally: use sqlite
    db_path = os.path.join(os.path.dirname(__file__), 'our.db')
    db_uri = 'sqlite:///{}'.format(db_path)
    debug_flag = True

print("Database url: ", db_uri)
print("namespace: ", os.environ)
#print("namespace 2: ", os.environ['NAMESPACE'])

SQLALCHEMY_DATABASE_URI = db_uri
SQLALCHEMY_TRACK_MODIFICATIONS = False
