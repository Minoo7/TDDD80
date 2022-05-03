from MyProject.server.main import create_obj, find_possible_logins
from MyProject.server.models import Customer, session
from MyProject.server.validation.schemas import register_schemas

# register_schemas()
# create_obj(Customer, {"username": "rafeb3233", "password": "acceptedPass123", "first_name": "Rafeb", "last_name": "Laban", "email": "rafeb3233@gmail.com", "gender": "Man", "phone_number": "0735156678", "business_type": "Cafe", "organization_number": "12345678911"})
cus1 = session.query(Customer).get(1)
# print(Customer.query.with_entities(Customer.id).all())
cus1.follow(session.query(Customer).get(2))
session.commit()
print([cus.id for cus in cus1.following])