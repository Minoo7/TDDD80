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


#espresso = Customer(username='ESP123', password='pass123', first_name='Elsa', last_name='Fredriksson', email="elsa123@espresso.se", gender='test', customer_number='KDB1245',
			#		phone_number='0769471214', business_type=BusinessTypes.cafe.name, organization_number='11043554')
#db.session.add(espresso)

#db.session.commit()
#ad = Address(address_type='home', street='Landsvägen 13', city='Lund', zip_code='192 71', customer_id=espresso.id)
#db.session.add(ad)
#db.session.commit()
#print("d: ", ad.id)
#print("a: ", espresso.address)


address_1 = Addressa(street="sreetHome", city="city", state="state", zip="192")
address_2 = Addressa(street="sreetWork", city="city", state="state", zip="192")
db.session.add(address_1)
db.session.add(address_2)
db.session.commit()

cua = Customera(billing_address_id=address_1.id)
db.session.add(cua)
db.session.commit()
from sqlalchemy import inspect
print(inspect(address_1.cid).attrs.keys())
print("yo: ", address_1.cid)
