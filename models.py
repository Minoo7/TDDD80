import datetime

from flask_marshmallow import Marshmallow, sqla as ma_sqla
from sqlalchemy import *
# from sqlalchemy.dialects import postgresql
from sqlalchemy.orm import sessionmaker, relationship

import Enums
# from server import ma, ma_sqla
from sqlalchemy.dialects.postgresql import ENUM
from flask_sqlalchemy import SQLAlchemy
from server import db

# SQLAlchemyAutoSchemaOpts = ma_sqla.SQLAlchemyAutoSchemaOpts
import server

#db = SQLAlchemy(server.app)
#ma = Marshmallow(server.app)
#
Model = db.Model


# read_messages = db.Table('read_messages', Model.metadata,
#                         Column('messages_id', Integer, ForeignKey('message.id')),
#                         Column('user_id', Integer, ForeignKey('user.id')))
# image_ids = db.Table('image_ids', Model.metadata, Column('image_id'), Integer, ForeignKey('image_id'))


class User(Model):  # User abstract class
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
	id = Column(Integer, primary_key=True)
	username = Column(String(32), nullable=False)
	password = Column(String(32), nullable=False)
	first_name = Column(String(32))
	last_name = Column(String(32))
	email = Column(String(150), nullable=False)
	gender = Column(ENUM(Enums.Genders))


class Author(Model):
	__tablename__ = "authors"
	id = Column(Integer, primary_key=True)
	name = Column(String)

	def __repr__(self):
		return "<Author(name={self.name!r})>".format(self=self)


# Relations:
# rel = relationship("Message", secondary=read_messages, backref="readBy", lazy=True)

# def to_dict(self):
#	return {'id': self.id, 'Name': self.name, 'read': [read.message for read in self.messages_read]}


"""class Administrator(User):
	__tablename__ = "administrators"
	permission_group = Column(db.Enum('group1', name='permission_groups'), nullable=False)
	"""


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

	# customer_number = Column(String(32), CheckConstraint('char_length(customer_number) IS 6'), nullable=False)
	customer_number = Column(String(10), nullable=False)
	phone_number = Column(String(20), nullable=False)
	business_type = Column(ENUM(Enums.BusinessTypes), nullable=False)
	organization_number = Column(String(11), nullable=False)

	address = relationship("Address",
	                       primaryjoin="and_(Customer.id==Address.customer_id, ""Address.address_type=='home')",
	                       post_update=True, uselist=False)
	business_address = relationship("Address",
	                                primaryjoin="and_(Customer.id==Address.customer_id, ""Address.address_type=='work')",
	                                post_update=True, overlaps="address", uselist=False)

	following = relationship(
		'Customer', lambda: follower_table,
		primaryjoin=lambda: Customer.id == follower_table.c.user_id,
		secondaryjoin=lambda: Customer.id == follower_table.c.following_id,
		backref='followers'
	)

	""""@validates('customer_number')
	def validate_customer_number(self, key, value):
		assert value.length == 6
		return value"""

	def __repr__(self):
		return '<Customer %r>' % self.customer_number


follower_table = Table(
	'user_following', Model.metadata,
	Column('user_id', Integer, ForeignKey('customers.id'), primary_key=True),
	Column('following_id', Integer, ForeignKey('customers.id'), primary_key=True)
)


class Address(Model):
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
	id = Column(Integer, primary_key=True)
	address_type = Column(Enum('home', 'work', name='address_types'), nullable=False)  # Home, Billing, Both
	street = Column(String(95), nullable=False)
	city = Column(String(35), nullable=False)
	zip_code = Column(String(11), nullable=False)

	apartment_number = Column(String(20))
	other_info = Column(String(255))

	customer_id = Column(Integer, ForeignKey('customers.id'))
	customer = relationship("Customer", foreign_keys=customer_id, post_update=True,
	                        overlaps="address,business_address")


class Comment(Model):
	"""
    PK  id
    FK  post_id
    FK  customer_id
    """
	__tablename__ = "comments"
	id = Column(Integer, primary_key=True)
	content = Column(String(120), nullable=False)
	post_id = Column(Integer, ForeignKey('posts.id'))
	user_id = Column(Integer, ForeignKey('customers.id'))
	created_at = Column(DateTime(), default=datetime.datetime.now(), nullable=False)


class Post(Model):
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
	__table_args__ = (CheckConstraint('NOT(content IS NULL AND image_id IS NULL)'),)
	id = Column(Integer, primary_key=True)
	user_id = Column(Integer, ForeignKey('customers.id'))
	content = Column(String(255))
	image_id = Column(Integer, ForeignKey('images.id'))
	type = Column(ENUM(Enums.PostTypes), nullable=False)
	created_at = Column(DateTime(), default=datetime.datetime.now(), nullable=False)
	updated_at = Column(DateTime())
	likes = relationship("Like", backref="post", uselist=True)
	comments = relationship("Comment", backref="post", uselist=True)


class Like(Model):
	"""
	PK	id
	FK	post_id
	FK	customer_id
	"""
	__tablename__ = "likes"
	id = Column(Integer, primary_key=True)
	post_id = Column(Integer, ForeignKey('posts.id'))
	user_id = Column(Integer, ForeignKey('customers.id'))


# class BaseOpts(SQLAlchemyAutoSchemaOpts):

"""class BaseOpts(ma_sqla.SQLAlchemyAutoSchemaOpts):
	def __init__(self, meta, ordered=False):
		if not hasattr(meta, "sqla_session"):
			meta.sqla_session = Session
		meta.include_fk = True
		meta.load_instance = True
		super(BaseOpts, self).__init__(meta, ordered=ordered)"""

"""class BaseSchema(ma.SQLAlchemyAutoSchema):
	OPTIONS_CLASS = BaseOpts"""


# class LikeSchema(BaseSchema):
#	class Meta:
#		model = Like


class ImageReference(Model):
	"""
	PK	id
		path
	"""
	__tablename__ = "images"
	id = Column(Integer, primary_key=True)
	path = Column(String(120), nullable=False)


class Feed(Model):
	"""
    PK  id
    FK  customer_id
    FK  post_id (multiple)
    """
	__tablename__ = "feeds"
	id = Column(Integer, primary_key=True)
	customer_id = Column(Integer, ForeignKey('customers.id'))


def setup_schema():
	# Create a function which incorporates the Base and session information
	def setup_schema_fn():
		# for class_ in models:
		for class_ in [x.class_ for x in Model.registry.mappers]:
			if hasattr(class_, "__tablename__"):
				if class_.__name__.endswith("Schema"):
					raise server.ma.sql.ModelConversionError(
						"For safety, setup_schema can not be used when a"
						"Model class ends with 'Schema'"
					)

				class Meta(object):
					model = class_
					sqla_session = server.db.session
					include_fk = True
					load_instance = True
				# transient = True

				class Opts(ma_sqla.SQLAlchemyAutoSchemaOpts):
					def __init__(self, meta, ordered=False):
						# if not hasattr(meta, "sqla_session"):
						# 	meta.sqla_session = Session
						"""meta.model = class_
						meta.sqla_session = session
						meta.include_fk = True
						meta.load_instance = True"""
						meta = Meta
						super().__init__(meta, ordered=ordered)

				Opts.__name__ = class_.__name__ + "Opts"

				schema_class_name = "%sSchema" % class_.__name__

				schema_class = type(
					schema_class_name, (server.ma.SQLAlchemyAutoSchema,), {"Meta": Meta}
				)

				setattr(class_, "__opts__", Opts)
				setattr(class_, "__marshmallow__", schema_class)

	return setup_schema_fn


setup_schema()()

# posts = relationship("Post")


# class Image()


"""class Message(Model):
	__tablename__ = "messages"
	id = Column(Integer, primary_key=True)
	message = Column(String(140))

	def to_dict(self):
		return {'id': self.id, 'message': self.message, 'readBy': [f"{usr.name} ({usr.id})" for usr in self.readBy]}
"""

"""
class Parent(Model):
	__tablename__ = 'parent'
	id = Column(Integer, primary_key=True)

	# previously one-to-many Parent.children is now
	# one-to-one Parent.child
	child = relationship("Child", back_populates="parent", uselist=False)


class Child(Model):
	__tablename__ = 'child'
	id = Column(Integer, primary_key=True)
	parent_id = Column(Integer, ForeignKey('parent.id'), nullable=False)

	# many-to-one side remains, see tip below
	parent = relationship("Parent", back_populates="child")"""
