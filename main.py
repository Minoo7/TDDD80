from models import *
# from server import db

def add_message(new_message, sender):
    message = Message(message=new_message)
    db.session.add(message)
    db.session.commit()
    return {'id': message.id}


def get_messages():
    return [msg.to_dict() for msg in Message.query.all()]

