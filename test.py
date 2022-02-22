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


#address_1 = Addressa(street="sreetHome", city="city", state="state", zip="192", type="home")
#address_2 = Addressa(street="sreetWork", city="city", state="state", zip="192", type="work")
# db.session.add(address_1)
# db.session.add(address_2)
# db.session.commit()

#db.session.add(address_1)
#db.session.add(address_2)
#db.session.commit()
#print("address_1.id: ", address_1.id)
cua = Customera()
db.session.add(cua)
db.session.commit()
print(cua.id)
address_1 = Addressa(street="sreetHome", city="city", state="state", zip="192", type="home", customera_id=cua.id)
address_2 = Addressa(street="sreetWork", city="city", state="state", zip="192", type="work", customera_id=cua.id)
db.session.add(address_1)
db.session.add(address_2)
db.session.commit()
#address_3 = Addressa(street="sreetWork", city="city", state="state", zip="192")
#db.session.flush()
#.print("k", address_3.id)
print(cua)
print("cua.billing_address: ", cua.billing_address.id)
print("cua.shipping_address: ", cua.shipping_address.id)
#print("address_1.customera_id:", address_1.customera_id)
#print("address_2.customera_id:", address_2.customera_id)
#print("yo: ", address_1.get_customer_id())
