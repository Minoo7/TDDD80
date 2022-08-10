from MyProject.server import db
from MyProject.server.routes.views import app


def reset_db():
	db.drop_all()
	db.create_all()
	return "", 200


if __name__ == "__main__":
	app.debug = True
	app.run(host='0.0.0.0', port=5080, debug=True)
