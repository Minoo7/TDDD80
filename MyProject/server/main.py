import random
import string

import groups
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