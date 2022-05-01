"""def add_message(new_message, sender):
    message = Message(message=new_message)
    db.session.add(message)
    db.session.commit()
    return {'id': message.id}


def get_messages():
    return [msg.to_dict() for msg in Message.query.all()]
"""

from MyProject.server import ValidationError
from MyProject.server.models import session
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


def delete_obj(class_, id_):
	session.delete(find(class_, id_))
	session.commit()
