from MyProject.server.main import create_obj, find_possible_logins
from MyProject.server.models import Customer
from MyProject.server.validation.schemas import register_schemas

register_schemas()
create_obj(Customer, {"username": "rafeb3233", "password": "acceptedPass123", "first_name": "Rafeb", "last_name": "Laban", "email": "rafeb3233@gmail.com", "gender": "Man", "phone_number": "0735156678", "business_type": "Cafe", "organization_number": "12345678911"})
find_possible_logins("rafeb3233")