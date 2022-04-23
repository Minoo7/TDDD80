# from MyProject.server import app
from MyProject.server.models import db
from MyProject.server.views import *


def reset_db():
	db.drop_all()
	db.create_all()
	return "", 200


if __name__ == "__main__":
	# init_views(app)
	reset_db()
	app.debug = True
	app.run(port=5080)
