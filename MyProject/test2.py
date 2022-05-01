import sqlalchemy.exc

from MyProject.server import groups
from MyProject.server.validation.schemas import PostSchema, CustomerSchema
from server.models import Customer, session

print(session.query(Customer).get(1).customer_number)
