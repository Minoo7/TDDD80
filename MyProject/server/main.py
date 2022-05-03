from MyProject.server import ValidationError
from MyProject.server.models import session, Customer
from MyProject.server.validation.validate import IdError


def query(class_, **attr):
	return session.query(class_).filter_by(**attr)


def find(class_, id_):
	return session.query(class_).get(id_)


def find_by(class_, **kwargs):
	return query(class_, **kwargs).first()


def find_by_all(class_, **kwargs):
	return query(class_, **kwargs).all()


def assert_id_exists(class_, id_):
	if not find(class_, id_):
		raise IdError(class_, id_)


def create_obj(class_, json_input):
	obj = class_.__schema__().load(json_input)
	session.add(obj)
	session.commit()


def edit_obj(class_, id_, json_input):
	schema = class_.__schema__
	errors = schema().validate(json_input, partial=True)
	if not bool(errors):  # no errors
		obj = find(class_, id_)
		for param in json_input:
			setattr(obj, param, json_input[param])
		session.commit()
	else:
		raise ValidationError(errors)


def get_obj_as_json(class_, id_, *args):
	schema = class_.__schema__
	obj = find(class_, id_)
	json = schema().dump(obj)
	"""for arg in args:
		json = json | {arg: getattr(obj, arg)}
		# print(getattr(obj, arg))"""
	return json


def delete_obj(class_, id_):
	session.delete(find(class_, id_))
	session.commit()


def find_possible_logins(login_method_name):
	methods = {'username', 'customer_number', 'email', 'organization_number'}
	return [users for method in methods for users in find_by_all(Customer, **{method: login_method_name})]
