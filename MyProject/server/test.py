from MyProject.server import db
from MyProject.server.models import Customer, Address
from MyProject.server.validation.validate import unique_customer_number


def add(username, email, phone_number, business_type, organization_number, business_name, address_type, street, city, zip_code):
	c = Customer(username=username, customer_number=unique_customer_number(), password="goodPass123", first_name="Patrik", last_name="Jonsson", email=email, gender="Man", phone_number=phone_number, business_type=business_type, organization_number=organization_number, business_name=business_name)
	db.session.add(c)
	db.session.commit()
	a = Address(address_type=address_type, street=street, city=city, zip_code=zip_code, customer_id=c.id)
	db.session.add(a)
	db.session.commit()


# add("icatyreso548", "icatyreso@gmail.com", "073516123", "Supermarket", "5565073805", "ICA Kvantum Tyresö", "Work", "Östangränd 2", "Tyresö", "135 40")

