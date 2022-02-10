"""def test():
	page = Nouns(
		name="word",
		runame="слово",
		variety=BusinessTypes(1).concrete)
	db.session.add(page)
	#request.dbsession.add(page)
"""

from server import *
from models import *

# from models import Nouns

from enum import Enum

BusinessTypes = Enum('BusinessTypes', 'restaurant cafe shop supermarket')


espresso = Customer(username='ESP123', password='pass123', first_name='Elsa', last_name='Fredriksson', email="elsa123@espresso.se", gender='test', customer_number='KDB1245',
					phone_number='0769471214', business_type=BusinessTypes.cafe.name, organization_number='11043554')
ad = Address(address_type='home', street='Landsvägen 13', city='Lund', zip_code='192 71')

db.session.add(espresso)
db.session.add(ad)
db.session.commit()
