"""def test():
	page = Nouns(
		name="word",
		runame="слово",
		variety=BusinessTypes(1).concrete)
	db.session.add(page)
	#request.dbsession.add(page)
"""
from server import groups
from server.models import session
from server .validate import CustomerSchema, AddressSchema, CommentSchema, PostSchema

"""espresso = Customer(username='ESP123', password='pass123', first_name='Elsa', last_name='Fredriksson',
                    email="elsa123@espresso.se", gender=Enums.Genders.Woman, customer_number='KDB1245',
                    phone_number='0769471214', business_type=Enums.BusinessTypes.Cafe, organization_number='11043554')
db.session.add(espresso)

db.session.commit()"""
"""vincent = Customer(username='VIN123', password='pass123', first_name='Vincent', last_name='Garbrant',
                   email='email@gmail.com', gender=Enums.Genders.Man, customer_number="VIKD123",
                   phone_number="070123123", business_type=Enums.BusinessTypes.Restaurant, organization_number=101010,
                   followers=[espresso])
db.session.add(vincent)
db.session.commit()
ad = Address(address_type='home', street='Landsvägen 13', city='Lund', zip_code='192 71', customer_id=espresso.id)
ad2 = Address(address_type='work', street='Work address', city='Lund', zip_code='192 71', customer_id=espresso.id)
db.session.add(ad)
db.session.add(ad2)
db.session.commit()

post = Post(user_id=espresso.id, content="hej", type=Enums.PostTypes.review)
db.session.add(post)
db.session.commit()

# like1 = Like(post_id=post.id, user_id=espresso.id)
# comment1 = Comment(content="Vilken rolig post!", post_id=post.id, user_id=espresso.id)
# db.session.add(like1)
# db.session.add(comment1)
db.session.commit()
# print(post.likes)
# print(post.comments)
# print(comment1.id)
print(espresso.following)
# espresso.followers.append(vincent)
db.session.commit()
print(espresso.followers)
print(vincent.followers)

print("---")

db.session.add(LikeSchema().load({'id': 2, 'post_id': 1, 'user_id': 1}))
db.session.commit()
print(Customer.__marshmallow__().dump(espresso))
# {'username': 'ESP123', 'email': 'elsa123@espresso.se', 'gender': <Genders.Woman: 2>, 'id': 33, 'customer_number': 'KDB1245', 'organization_number': '11043554', 'last_name': 'Fredriksson', 'password': 'pass123', 'business_type': <BusinessTypes.Cafe: 2>, 'phone_number': '0769471214', 'first_name': 'Elsa'}
"""

dct = {'username': 'vincent122345', 'email': 'loaded@email.com', 'gender': 'Man',
       'organization_number': 11043554910, 'last_name': 'Fredriksson',
       'password': 'pass123', 'business_type': 'Cafe', 'phone_number': '+46769471214',
       'first_name': 'Elsa'}
# add_customer(dct)
usr = CustomerSchema().load(dct)
session.add(usr)
session.commit()

testUser = {'username': 'vincent12345', 'password': 'apelsin12345', 'first_name': 'Vincent', 'last_name': 'Garbrant',
            'email': 'rafeb@gmail.com', 'gender': 'Man', 'customer_number': 'ABC123',
            'phone_number': '+46769471213', 'business_type': 'Cafe',
            'organization_number': 12345678911}

d = CustomerSchema().load(testUser)
# session.add(d)
# session.commit()

# cus = CustomerSchema().load(dct)
# print(server.counter)
# db.session.add(cus)
# db.session.commit()
# post = Post(user_id=cus.id, content="hej", type=Enums.PostTypes.review)
"""post = PostSchema().load({'content': 'hej', 'id': 1, 'type': Enums.PostTypes.review, 'image_id': None, 'user_id': 1, 'created_at': '2022-04-20T17:54:49.226500', 'updated_at': None}
)
db.session.add(post)
db.session.commit()
ad = Address(address_type='home', street='Landsvägen 13', city='Lund', zip_code='192 71', customer_id=1)
AddressSchema().load({'city': 'Lund', 'id': 1, 'other_info': None, 'customer_id': 1, 'zip_code': '192 71', 'street': 'Landsvagen 13', 'address_type': Enums.AddressTypes.home}
)
co = Comment(content="what a great post!", post_id=1, user_id=1, created_at=datetime.datetime.now())
s = CommentSchema().load({'id': 1, 'created_at': '2022-04-20T16:26:04.171327', 'post_id': 1, 'content': 'what a great post!', 'user_id': 1})
db.session.add(s)
db.session.add(co)
db.session.add(ad)
db.session.commit()


# print(repr(Enums.Genders.Woman) in Enums.Genders.__members__.values())
# print(repr(Enums.Genders.Woman))
# print(Enums.Genders.__members__.values())
# print(repr(Enums.Genders.Woman) in Enums.Genders._value2member_map_.items())
# print(Enums.Genders(Enums.Genders.Woman))
# print(repr())
# print(Customer.__marshmallow__().load())"""
