from datetime import datetime

from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import *
from sqlalchemy.orm import relationship, sessionmaker, deferred

from MyProject.server import groups, Model
from sqlalchemy.dialects.postgresql import ENUM

# --- settings for model ---
Model._validation = True
Model.id = Column(Integer, primary_key=True)


class TokenBlocklist(Model):
	id = Column(Integer, primary_key=True)
	jti = Column(String(36), nullable=False)
	created_at = Column(DateTime, nullable=False)


TokenBlocklist._validation = False


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
	customer_number = Column(String(6), unique=True, nullable=False)
	image_url = Column(String(120))
	phone_number = Column(String(20), unique=True, nullable=False)
	business_type = Column(ENUM(groups.BusinessTypes), nullable=False)
	organization_number = Column(String(11), unique=True, nullable=False)
	business_name = Column(String(50), unique=True, nullable=False)
	bio = Column(String(120))

	address = relationship("Address",
						   primaryjoin="and_(Customer.id==Address.customer_id, Address.address_type=='Home')",
						   post_update=True, cascade="all, delete", uselist=False)
	business_address = relationship("Address",
									primaryjoin="and_(Customer.id==Address.customer_id, ""Address.address_type=='Work')",
									post_update=True, overlaps="address", cascade="all, delete", uselist=False)
	posts = relationship("Post")

	following = relationship(
		'Customer', lambda: follower_table,
		primaryjoin=lambda: Customer.id == follower_table.c.customer_id,
		secondaryjoin=lambda: Customer.id == follower_table.c.following_id,
		backref='followers'
	)

	def follow(self, follow_customer):
		if follow_customer.id == self.id:
			raise ValueError("can't follow self")
		self.following.append(follow_customer)

	def unfollow(self, follow_customer):
		self.following.pop(follow_customer, None)

	def has_address(self, address_type: groups.AddressTypes):
		if address_type == groups.AddressTypes.Home:
			return self.address is not None
		if address_type == groups.AddressTypes.Work:
			return self.business_address is not None

	def get_profile(self):
		return self.__schema__(
			only=["username", "image_url", "business_type", "business_name", "followers", "following", "posts", "bio"]).dump(self)

	def get_mini(self):
		return self.__schema__(only=["id", "username", "image_url"]).dump(self)

	def get_feed(self):
		return Post.__schema__(many=True).dump(
			Post.query.join(follower_table, (follower_table.c.following_id == Post.customer_id)).filter(
				follower_table.c.customer_id == self.id).order_by(Post.created_at).all())


follower_table = Table(
	'user_following', Model.metadata,
	Column('customer_id', Integer, ForeignKey('customers.id'), primary_key=True),
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
	street = Column(String(95), unique=True, nullable=False)
	city = Column(String(35), nullable=False)
	zip_code = Column(String(11), nullable=False)

	other_info = Column(String(255))

	customer_id = Column(Integer, ForeignKey('customers.id'))
	customer = relationship("Customer", foreign_keys=customer_id, post_update=True,
							overlaps="address, business_address")


class Comment(Model):
	"""
    PK  id
    FK  post_id
    FK  customer_id
    	content
    	created_at
    """
	__tablename__ = "comments"
	post_id = Column(Integer, ForeignKey('posts.id'), nullable=False)
	customer_id = Column(Integer, ForeignKey('customers.id'), nullable=False)
	content = Column(String(120), nullable=False)
	created_at = Column(DateTime(), default=datetime.now(), nullable=False)


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
	customer_id = Column(Integer, ForeignKey('customers.id'), nullable=False)
	image_id = Column(Integer, ForeignKey('images.id'))
	title = Column(String(40), nullable=False)
	content = Column(String(255))
	type = Column(ENUM(groups.PostTypes), nullable=False)
	created_at = Column(DateTime(), default=datetime.now(), nullable=False)
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
	customer_id = Column(Integer, ForeignKey('customers.id'))

	def get_parent(self):
		return self.post_id


class ImageReference(Model):
	"""
	PK	id
		path
	"""
	__tablename__ = "images"
	path = Column(String(120), nullable=False)
