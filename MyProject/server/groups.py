from enum import Enum


class BusinessTypes(Enum):
	Restaurant = 1
	Cafe = 2
	Shop = 3
	Supermarket = 4


class Genders(Enum):
	Man = 1
	Woman = 2
	Non_binary = 3
	Other = 4


class PostTypes(Enum):
	review = 1


class AddressTypes(Enum):
	Home = 1
	Work = 2
