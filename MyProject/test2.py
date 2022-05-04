from MyProject.server.validation.schemas import register_schemas

register_schemas()

from MyProject.server.models import Customer

print(Customer.query.get(1).get_feed())
