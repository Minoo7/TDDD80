import random
import string

import Enums
from models import *
# from server import *
from flask_wtf import FlaskForm
from password_validator import PasswordValidator
from email_validator import validate_email, EmailNotValidError

from validate import *
from models import *

"""def add_message(new_message, sender):
    message = Message(message=new_message)
    db.session.add(message)
    db.session.commit()
    return {'id': message.id}


def get_messages():
    return [msg.to_dict() for msg in Message.query.all()]
"""


def id_generator(size=6, chars=string.digits):
	return ''.join(random.choice(chars) for _ in range(size))


def add_customer(username: string, password: string, first_name: string, last_name: string, email: string,
                 gender: Enums.Genders, phone_number: string, business_type: Enums.BusinessTypes,
                 organization_number: int):
	if not validate_username(username):
		raise InputNotValidError
	if not PasswordValidator().validate(password):
		raise InputNotValidError
	if not (validate_name(first_name) and validate_name(last_name)):
		raise InputNotValidError
	try:
		# Validate.
		valid = validate_email(email)

		# Update with the normalized form.
		email = valid.email
	except EmailNotValidError as e:
		# email is not valid, exception message is human-readable
		print(str(e))
		raise InputNotValidError
	if not isinstance(gender, Enums.Genders):
		raise InputNotValidError
	if not AbstractPhoneValidation.verify(phone_number).valid:
		raise InputNotValidError
	if not isinstance(business_type, Enums.BusinessTypes):
		raise InputNotValidError
	if not validate_organization_number(organization_number):
		raise InputNotValidError

	generated_id = id_generator()
	# don't want to generate existing customer_number:
	while db.session.query(Customer.id).filter_by(customer_number=generated_id).first() is not None:
		generated_id = id_generator()
	customer = Customer(username=username, password=password, first_name=first_name, last_name=last_name,
	                    email=email, gender=gender, customer_number=generated_id,
	                    phone_number=phone_number, business_type=business_type,
	                    organization_number=organization_number)
	db.session.add(customer)
	db.session.commit()

# if form.validate_on_submit():
#	print("hej")
#	return redirect('/success')
# return render_template('submit.html', form=form)

# add_customer("us", "pas", "espes", "garbr", "email", "gend")
