from flask_marshmallow import Marshmallow
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import sessionmaker

from MyProject.my_package.views import init_views
from my_package import app

def reset_db():
    db.drop_all()
    db.create_all()
    return "", 200

if __name__ == "__main__":
    reset_db()
    app = init_views(app)
    app.debug = True
    app.run(port=5080)
