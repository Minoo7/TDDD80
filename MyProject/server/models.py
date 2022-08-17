from datetime import datetime

from sqlalchemy import *
from sqlalchemy.orm import relationship

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
	username = Column(Text, unique=True, nullable=False)
	password = Column(Text, nullable=False)
	first_name = Column(Text)
	last_name = Column(Text)
	email = Column(Text, unique=True, nullable=False)
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
	customer_number = Column(Text, unique=True, nullable=False)
	image_id = Column(Integer, ForeignKey('images.id'))
	phone_number = Column(Text, unique=True, nullable=False)
	business_type = Column(ENUM(groups.BusinessTypes), nullable=False)
	organization_number = Column(Text, unique=True, nullable=False)
	business_name = Column(Text, unique=True, nullable=False)
	bio = Column(Text)

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
		print("yoo")
		print(follow_customer)
		print(follow_customer.id)
		self.following.remove(follow_customer)

	def has_address(self, address_type: groups.AddressTypes):
		if address_type == groups.AddressTypes.Home:
			return self.address is not None
		if address_type == groups.AddressTypes.Work:
			return self.business_address is not None

	def get_profile(self):
		return self.__schema__(
			only=["id", "username", "image_id", "business_type", "business_name", "followers", "following", "posts", "bio"]).dump(self)

	def get_mini(self):
		return self.__schema__(only=["id", "username", "image_id"]).dump(self)

	def get_feed(self):
		following_posts = Post.query.join(follower_table, (follower_table.c.following_id == Post.customer_id)).filter(
				follower_table.c.customer_id == self.id)
		self_posts = Post.query.filter_by(customer_id=self.id)
		posts_to_show = following_posts.union(self_posts).order_by(desc(Post.created_at)).all()
		return Post.__schema__(many=True).dump(posts_to_show)


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
	created_at = Column(DateTime(), nullable=False)


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
	location = Column(String(255))
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
