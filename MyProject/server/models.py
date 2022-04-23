import datetime

from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import *
# from sqlalchemy.dialects import postgresql
from sqlalchemy.orm import relationship, sessionmaker

from MyProject.server import app, groups
from sqlalchemy.dialects.postgresql import ENUM

db = SQLAlchemy(app)
# db.session = sessionmaker(bind=db.get_engine(app=app), autoflush=True)()
db.Model.id = Column(Integer, primary_key=True)
Model = db.Model
session = db.session

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
	username = Column(String(32), unique=True, nullable=False)
	password = Column(String(32), nullable=False)
	first_name = Column(String(32))
	last_name = Column(String(32))
	email = Column(String(150), unique=True, nullable=False)
	gender = Column(ENUM(groups.Genders))

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
	customer_number = Column(String(6), unique=True)
	phone_number = Column(String(20), unique=True, nullable=False)
	business_type = Column(ENUM(groups.BusinessTypes), nullable=False)
	organization_number = Column(String(11), unique=True, nullable=False)

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
        other_info # optional

        <-Relations->:
        customer_id
    """
	__tablename__ = "addresses"
	address_type = Column(ENUM(groups.AddressTypes), nullable=False)  # Home, Billing, Both
	street = Column(String(95), nullable=False)
	city = Column(String(35), nullable=False)
	zip_code = Column(String(11), nullable=False)

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
	# __table_args__ = (CheckConstraint('NOT(content IS NULL AND image_id IS NULL)'),)
	user_id = Column(Integer, ForeignKey('customers.id'))
	image_id = Column(Integer, ForeignKey('images.id'))
	content = Column(String(255))
	type = Column(ENUM(groups.PostTypes), nullable=False)
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
	post_id = Column(Integer, ForeignKey('posts.id'))
	user_id = Column(Integer, ForeignKey('customers.id'))


class ImageReference(Model):
	"""
	PK	id
		path
	"""
	__tablename__ = "images"
	path = Column(String(120), nullable=False)


class Feed(Model):
	"""
    PK  id
    FK  customer_id
    FK  post_id (multiple)
    """
	__tablename__ = "feeds"
	customer_id = Column(Integer, ForeignKey('customers.id'))

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
