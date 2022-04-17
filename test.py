"""def test():
	page = Nouns(
		name="word",
		runame="слово",
		variety=BusinessTypes(1).concrete)
	db.session.add(page)
	#request.dbsession.add(page)
"""
import inspect
import numbers

from flask_marshmallow import Schema
from flask_marshmallow.sqla import auto_field
from marshmallow import ValidationError, fields, post_load, validates as validater

import Enums
import custom_fields
from server import *
from models import *

# from models import Nouns

from enum import Enum

espresso = Customer(username='ESP123', password='pass123', first_name='Elsa', last_name='Fredriksson',
                    email="elsa123@espresso.se", gender=Enums.Genders.Woman, customer_number='KDB1245',
                    phone_number='0769471214', business_type=Enums.BusinessTypes.Cafe, organization_number='11043554')
db.session.add(espresso)

db.session.commit()
vincent = Customer(username='VIN123', password='pass123', first_name='Vincent', last_name='Garbrant',
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

"""author = Author(name="Chuck Paluhniuk")
db.session.add(author)
db.session.commit()
author_schema = Author.__marshmallow__()

print(author_schema.dump(author))"""

"""class TMeta(object):
    model = Author
    sqla_session = session
    include_fk = True
    load_instance = True

class ATest(author_schema):
    def __init__(self):
        self.meta = TMeta()"""

"""gg = ATest()
# d = author_schema.load({'id': 69, 'name': 'vincent'}, session=db.session)
d = gg.load({'id': 69, 'name': 'vincent'}, session=db.session)
db.session.add(d)
db.session.commit()"""

# LikeSch = type('LikeSch', Like.__marshmallow__().__bases__, dict(Like.__marshmallow__().__dict__))
# LikeSch = type("LikeSch", (Like.__marshmallow__(),), {})


"""class MyMetaClass(type(Like.__marshmallow__())):
	pass


class LSchema(Like.__marshmallow__(), metaclass=MyMetaClass):

	def func(self):
		return 32"""

"""from copy import deepcopy

def copy_class(cls, new_name):
   new_cls = type(new_name, cls.__bases__, deepcopy(A.__dict__))
   new_cls.__name__ = new_name
   return new_cls"""

"""class dBaseOpts(ma_sqla.SQLAlchemyAutoSchemaOpts):
    def __init__(self, meta, ordered=False):
        if not hasattr(meta, "sqla_session"):
            meta.sqla_session = Session
        super(dBaseOpts, self).__init__(meta, ordered=ordered)


class BaseSchema(ma.SQLAlchemyAutoSchema):
    OPTIONS_CLASS = dBaseOpts


class UserSchema(BaseSchema):
    pass
    # class Meta:
     #    model = User"""


# def in_db(**kwargs)



class LikeSchema(ma.SQLAlchemyAutoSchema):
	Meta = Like.__marshmallow__().Meta

	id = fields.Integer(strict=True)
	post_id = custom_fields.post_id()
	user_id = custom_fields.customer_id()

	"""@validater('post_id')
	def validate_post_id(self, post_id):
		if post_id < 5:
			raise ValidationError('The age is too old!')"""


"""def addSchemeOptions(class_):
	if isinstance(class_, db.Model):
		#class_.__marshmallow__()
		class NewScheme(ma.SQLAlchemyAutoSchema):
			Meta = class_.__marshmallow__()
"""

# LSchema = type("LSchema", (ma.SQLAlchemyAutoSchema,), {"Meta": Like.__marshmallow__().Meta})
# LSchema = copy_class(Like.__marshmallow__(), "LSchema")
"""ls = Like.__marshmallow__()

ls.sample = hello.__get__(ls)

ls.sample()"""

like_schema = LikeSchema()
# like_schema.Metaload_instance = False

# setattr(like_schema.Meta, "load_instance", True)
# print(like_schema.Meta.load_instance)
# like_schema = LikeSch()
# like_schema.Meta.load_instance = property(lambda self: True)
# print(like_schema.Meta.load_instance)
# print(like_schema.Meta.include_fk)
print(like_schema.Meta.model)
l = like_schema.load({'id': '6', 'post_id': 6, 'user_id': 1})
# db.session.add(l)
# db.session.commit()

# print(Like.__marshmallow__().Meta.load_instance)

# d = LikeSchema().load({'post_id': '3', 'user_id': '4'}, session=db.session)
# db.event.listen(mapper, "after_configured", setup_schema(Base, db.session))
# @db.event.listens_for()
# def hi():
# print("lets goo")


# event.listen(db.mapper, "after_configured", setup_schema(Base, db.session))
# setup_schema(Base, db.session)()

"""like_schema = Like.__marshmallow__()
d = like_schema.load({'post_id': '3', 'user_id': '4'}, session=db.session)
db.session.add(d)
db.session.commit()
print(d)"""
