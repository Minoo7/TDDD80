from MyProject.server import db, session
from MyProject.server.routes.views import app


def reset_db():
	db.drop_all()
	db.create_all()
	return "", 200


if __name__ == "__main__":
	# reset_db()
	app.debug = True
	app.run(port=5080, debug=True)
