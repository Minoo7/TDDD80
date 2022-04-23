from enum import Enum

from server import groups, validate
from server.models import Customer, session

customer = Customer(username='VIN123', password='pass123', first_name='Vincent', last_name='Garbrant',
                   email='email@gmail.com', gender=groups.Genders.Man, customer_number="VIKD123",
                   phone_number="070123123", business_type=groups.BusinessTypes.Restaurant, organization_number=101010)
session.add(customer)
session.commit()

validate.setup_schema()()

# print(Customer.__marshmallow__().dump(customer))


print(groups.BusinessTypes(groups.BusinessTypes.Cafe))
