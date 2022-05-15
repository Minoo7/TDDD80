from MyProject.server import db, session
from MyProject.server.main import create_obj
from MyProject.server.models import Customer, Address, Post
from MyProject.server.routes.views import app


def reset_db():
	db.drop_all()
	db.create_all()
	return "", 200


def add_examples():
	# --- Customer 1 ---
	create_obj(Customer, {"username": "rafeb3233", "bio": "hello this is my biooo", "password": "goodPass123", "first_name": "rafeb", "last_name": "Laban", "email": "rafeb3233@gmail.com", "gender": "Man", "phone_number": "0735156678", "business_type": "Cafe", "organization_number": "12345678911", "business_name": "Savolax"})
	create_obj(Address, {"address_type": "home", "street": "Landsvägen 13", "city": "Lund", "zip_code": "192 71", "customer_id": 1})
	customer1 = Customer.query.get(1)

	# --- Customer 2 ---
	create_obj(Customer, {"business_name": "labanRestaurant", "username": "laban123212", "password": "goodPass123", "first_name": "Laban", "last_name": "Rafeb", "email": "laban123212@gmail.com", "gender": "Man", "phone_number": "0735126678", "business_type": "Cafe", "organization_number": "12145678911"})
	customer2 = Customer.query.get(2)

	# customer1 follow customer2
	customer1.follow(customer2)
	session.commit()

	# customer1 add post
	create_obj(Post, {"title": "post title yey", "content": "hejsan svejsan", "type": "review", "customer_id": 1})


if __name__ == "__main__":
	# init_views(app)
	reset_db()
	add_examples()
	app.debug = True
	app.run(host='0.0.0.0', port=5080)
