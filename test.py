from server import *
from models import *


# espresso = Customer(username='ESP123', password='pass123', first_name='Elsa', last_name='Fredriksson',
#					email="elsa123@espresso.se", gender='test', customer_number='KDB1245',
#					phone_number='0769471214', business_type='dh', organization_number='11043554')
# from models import Nouns

from enum import Enum

Animal = Enum('Animal', 'ant bee cat dog')

Animal.ant  # returns <Animal.ant: 1>
Animal['ant']  # returns <Animal.ant: 1> (string lookup)
Animal.ant.name  # returns 'ant' (inverse lookup)

def test():
	page = Nouns(
		name="word",
		runame="слово",
		variety=BusinessTypes(1).concrete)
	db.session.add(page)
	#request.dbsession.add(page)
#test()
print(Animal.ant.name)
t = Test(pyth=Animal.ant.name)
db.session.add(t)
db.session.commit()
