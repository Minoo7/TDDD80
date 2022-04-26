import sqlalchemy.exc

from MyProject.server import groups
from server.models import Customer, session

customer = session.query(Customer).filter_by(id=1).first()
print(customer)
print(customer.address)
print(groups.Genders["Man"])
