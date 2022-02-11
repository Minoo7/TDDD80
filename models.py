from sqlalchemy.dialects import postgresql

from server import db
from sqlalchemy.types import Enum as SQLAlchemyEnumType
# import json

from sqlalchemy.dialects.postgresql import ENUM
from enum import Enum


# read_messages = db.Table('read_messages', db.Model.metadata,
#                         db.Column('messages_id', db.Integer, db.ForeignKey('message.id')),
#                         db.Column('user_id', db.Integer, db.ForeignKey('user.id')))


class User(db.Model):  # User abstract class
	"""
	username
	password
	first_name
	last_name
	email
	gender
	"""
	__tablename__ = "users"
	__abstract__ = True
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(32), nullable=False)
	password = db.Column(db.String(32), nullable=False)
	first_name = db.Column(db.String(32))
	last_name = db.Column(db.String(32))
	email = db.Column(db.String(150), nullable=False)
	gender = db.Column(db.Enum('test', name='genders'))


# Relations:
# rel = db.relationship("Message", secondary=read_messages, backref="readBy", lazy=True)

# def to_dict(self):
#	return {'id': self.id, 'Name': self.name, 'read': [read.message for read in self.messages_read]}


class Administrator(User):
	__tablename__ = "administrators"
	permission_group = db.Column(db.Enum('group1', name='permission_groups'), nullable=False)


BusinessTypes = Enum('BusinessTypes', 'restaurant cafe shop supermarket')


class Customer(User):
	"""
	PK  id *
	FK  username *
	FK  password *
	FK  first_name *
	FK  last_name *
	FK  email *
	FK  gender *
		customer_number *
		phone_number *
		business_type *
		organization_number *

		<-Relations->:
		address_id
		business_address_id
	"""
	__tablename__ = "customers"

	customer_number = db.Column(db.String(32), nullable=False)
	phone_number = db.Column(db.String(20), nullable=False)
	business_type = db.Column(db.Enum(BusinessTypes), nullable=False)
	organization_number = db.Column(db.String(11), nullable=False)

# address_id = db.relationship("Address", back_populates="customer", uselist=False)

# address = db.relationship("Address", back_populates="customer", uselist=False)

# business_address = db.relationship("Address", back_populates="customer", uselist=False)


# dest_address = db.relationship('Address', backref='orders_dest_address')
# from_address = db.relationship('Address', backref='orders_from_address')


class BusinessTypes2(Enum):
	abstract = 1
	proper = 2
	concrete = 3
	collective = 4,
	compound = 5


Animal = Enum('Animal', 'ant bee cat dog')

Animal.ant  # returns <Animal.ant: 1>
Animal['ant']  # returns <Animal.ant: 1> (string lookup)
Animal.ant.name  # returns 'ant' (inverse lookup)


class Test(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	pyth = db.Column(db.Enum(Animal), nullable=False)


class Nouns(db.Model):
	__tablename__ = 'nouns'
	id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.Text())
	runame = db.Column(db.Text())
	variety = db.Column("variety", ENUM(BusinessTypes2, name='variety_enum'))


class Parent(db.Model):
	__tablename__ = 'parent'
	id = db.Column(db.Integer, primary_key=True)

	# previously one-to-many Parent.children is now
	# one-to-one Parent.child
	child = db.relationship("Child", back_populates="parent", uselist=False)


class Child(db.Model):
	__tablename__ = 'child'
	id = db.Column(db.Integer, primary_key=True)
	parent_id = db.Column(db.Integer, db.ForeignKey('parent.id'), nullable=False)

	# many-to-one side remains, see tip below
	parent = db.relationship("Parent", back_populates="child")


class Address(db.Model):
	"""
    PK  id
        address_type *
        street *
        city *
        zip_code *
        apartment_number # optional
        other_info # optional

        <-Relations->:
        customer_id
    """
	__tablename__ = "addresses"
	id = db.Column(db.Integer, primary_key=True)
	address_type = db.Column(db.Enum('home', name='address_types'), unique=True, nullable=False)  # Home, Billing, Both
	street = db.Column(db.String(95), nullable=False)
	city = db.Column(db.String(35), nullable=False)
	zip_code = db.Column(db.String(11), nullable=False)

	apartment_number = db.Column(db.String(20))
	other_info = db.Column(db.String(255))

	# customer_id = db.Column(db.Integer, db.ForeignKey('customer.id'))
	# customer = db.relationship("Customer", back_populates="address")

	#customer_id = db.Column(db.Integer, db.ForeignKey('customers.id'), nullable=False)
	#customer_id = db.Column(db.Integer, db.ForeignKey('customer.id'), nullable=False)


# many-to-one side remains, see tip below
# customer = db.relationship("Customer", back_populates="address")


class Customera(db.Model):
	__tablename__ = 'customera'
	id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.String)

	billing_address_id = db.Column(db.Integer, db.ForeignKey("addressa.id"))
	shipping_address_id = db.Column(db.Integer, db.ForeignKey("addressa.id"))

	billing_address = db.relationship("Addressa", foreign_keys=[billing_address_id], backref="cid")
	#shipping_address = db.relationship("Addressa", foreign_keys=[shipping_address_id], backref="cid")


class Addressa(db.Model):
	__tablename__ = 'addressa'
	id = db.Column(db.Integer, primary_key=True)
	street = db.Column(db.String)
	city = db.Column(db.String)
	state = db.Column(db.String)
	zip = db.Column(db.String)
	#customer_id = db.Column(db.Integer, db.ForeignKey('customera.id'), nullable=False)


class Post(db.Model):
	"""
    PK  id
    FK  customer_id
        content (either content or image needs to exist)
    FK  image_id (either content or image needs to exist)
        type *
        created_at *
        updated_at

        <-Relations->:
        total likes
        total comments
    """
	__tablename__ = "posts"
	id = db.Column(db.Integer, primary_key=True)
	# FK customer_id
	content = db.Column(db.String(255))
	# Relation image_id = db.Column(db.Integer)
	type = db.Column(db.Enum("front", name='post_types'), nullable=False)
	created_at = db.Column(db.DateTime(), nullable=False)
	updated_at = db.Column(db.DateTime())


	# total likes
	# total comments


class Feed(db.Model):
	"""
    PK  id
    FK  customer_id
    FK  post_id (multiple)
    """
	__tablename__ = "feeds"
	id = db.Column(db.Integer, primary_key=True)


# customer_id #FK
# post_id #FK (list: multiple)


class Follower(db.Model):
	"""
    PK  id
    Fk  customer_id
    FK  customer_follower_id
    """
	__tablename__ = "followers"
	id = db.Column(db.Integer, primary_key=True)


# FK customer_id
# FK customer_follower_id


class Comment(db.Model):
	"""
    PK  id
    FK  post_id
    FK  customer_id
    """
	__tablename__ = "comments"
	id = db.Column(db.Integer, primary_key=True)
	# FK post_id
	# FK customer_id
	created_at = db.Column(db.DateTime(), nullable=False)


class Like(db.Model):
	"""
    PK  id
    FK  post_id
    FK  user_id
    """
	__tablename__ = "likes"
	id = db.Column(db.Integer, primary_key=True)


# FK post_id
# FK user_id


# class Image()


"""class Message(db.Model):
	__tablename__ = "messages"
	id = db.Column(db.Integer, primary_key=True)
	message = db.Column(db.String(140))

	def to_dict(self):
		return {'id': self.id, 'message': self.message, 'readBy': [f"{usr.name} ({usr.id})" for usr in self.readBy]}
"""
