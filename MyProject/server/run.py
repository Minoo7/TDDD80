from MyProject.server.main import create_obj
from MyProject.server.models import db, Customer, Address
from MyProject.server.routes.views import app


def reset_db():
	db.drop_all()
	db.create_all()
	return "", 200


def add_examples():
	create_obj(Customer, {"username": "rafeb3233", "password": "goodPass123", "first_name": "Rafeb", "last_name": "Laban", "email": "rafeb3233@gmail.com", "gender": "Man", "phone_number": "0735156678", "business_type": "Cafe", "organization_number": "12345678911"})
	create_obj(Address, {"address_type": "home", "street": "Landsvagen 13", "city": "Lund", "zip_code": "192 71"})
	create_obj(Customer, {"username": "laban123212", "password": "goodPass123", "first_name": "Laban", "last_name": "Rafeb", "email": "laban123212@gmail.com", "gender": "Man", "phone_number": "0735126678", "business_type": "Cafe", "organization_number": "12145678911"})

if __name__ == "__main__":
	# init_views(app)
	reset_db()
	add_examples()
	app.debug = True
	app.run(port=5080)
