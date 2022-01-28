import os
import tempfile
import pytest
# from server import app
import server
from server import User, Message


@pytest.fixture
def client():
	db_fd, name = tempfile.mkstemp()
	server.app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + str(name)
	server.app.config['TESTING'] = True
	with server.app.test_client() as client:
		with server.app.app_context():
			server.db.create_all()
		yield client
	os.close(db_fd)
	os.unlink(name)


def test_save_message(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json')
	get_id = lambda msg: msg.get_json()['id']

	message_text = "hello"
	message = send_msg(message_text)
	message_id = get_id(message)
	assert message_id == 1

	message2_text = "this is a message"
	message2 = send_msg(message2_text)
	message2_id = get_id(message2)
	assert message2_id == 2


def test_get_msg(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json').get_json()
	get_id = lambda msg: msg['id']
	get_msg = lambda msg_id: client.get('/messages/' + str(msg_id), content_type='application/json').get_json()
	get_msg_text = lambda msg: msg['message']

	message_text = "hello"
	message = send_msg(message_text)
	message_id = get_id(message)
	received_msg = get_msg(message_id)
	assert get_msg_text(received_msg) == message_text
	assert get_id(received_msg) == message_id


def test_delete_msg(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json').get_json()
	get_id = lambda msg: msg['id']
	get_msg = lambda msg_id: client.get('/messages/' + str(msg_id), content_type='application/json')
	get_msg_text = lambda msg: msg['message']
	delete_msg = lambda msg_id: client.delete('/messages/' + str(msg_id), content_type='application/json')

	message_text = "hello"
	message = send_msg(message_text)
	message_id = get_id(message)
	received_msg = get_msg(message_id).get_json()

	assert get_msg_text(received_msg) == message_text  # Message exists

	# Delete message
	delete_msg(message_id)
	assert get_msg(message_id).status_code == 404  # Message not found in database


def test_get_all_msgs(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json').get_json()
	get_id = lambda msg: msg['id']
	get_msg = lambda msg_id: client.get('/messages/' + str(msg_id), content_type='application/json').get_json()
	get_msg_text = lambda msg: msg['message']
	delete_msg = lambda msg_id: client.delete('/messages/' + str(msg_id), content_type='application/json')
	get_msgs = lambda: client.get('/messages', content_type='application/json').get_json()

	message_text = "hello"
	print(f'\nSending a message: "{message_text}"...')
	message = send_msg(message_text)

	message2_text = "this is a message"
	print(f'Sending another message: "{message2_text}"...')
	message2 = send_msg(message2_text)

	assert len(get_msgs()) == 2

	print(f"Deleting message by id: '{get_id(message)}'")
	delete_msg(get_id(message))

	print("Check that the total amount of messages is 1")
	assert len(get_msgs()) == 1


def test_add_user(client):
	add_user = lambda usr: client.post('/user/' + usr, content_type='application/json').get_json()
	get_id = lambda usr: usr['id']

	vincent = add_user("Vincent")
	vincent_id = get_id(vincent)
	print("\nAdding user Vincent..")
	print("User id: ", vincent_id)


def test_read_msg(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json').get_json()
	add_user = lambda usr: client.post('/user/' + usr, content_type='application/json').get_json()
	get_id = lambda msg: msg['id']
	get_msg = lambda msg_id: client.get('/messages/' + str(msg_id), content_type='application/json').get_json()
	get_msg_text = lambda msg: msg['message']
	get_msgs = lambda: client.get('/messages', content_type='application/json').get_json()
	read_msg = lambda msg_id, user_id: client.post("/messages/" + str(msg_id) + "/read/" + str(user_id))

	message_text = "hello"
	print(f'\nSending a message: "{message_text}"...')
	message = send_msg(message_text)
	message_id = get_id(message)

	vincent = add_user("Vincent")
	vincent_id = get_id(vincent)
	print("\nAdding user Vincent..")
	print("User id: ", vincent_id)

	print("Reading message")
	read_msg(message_id, vincent_id)

	# User Vincent should now be in msg readBy list
	print(get_msg(message_id))
	assert get_msg(message_id)['readBy'] == ["Vincent (" + str(vincent_id) + ")"]


def test_get_unread_msg(client):
	send_msg = lambda msg: client.post('/messages', json={'message': msg}, content_type='application/json').get_json()
	add_user = lambda usr: client.post('/user/' + usr, content_type='application/json').get_json()
	get_id = lambda msg: msg['id']
	get_msg = lambda msg_id: client.get('/messages/' + str(msg_id), content_type='application/json').get_json()
	get_msg_text = lambda msg: msg['message']
	get_msgs = lambda: client.get('/messages', content_type='application/json').get_json()
	read_msg = lambda msg_id, user_id: client.post("/messages/" + str(msg_id) + "/read/" + str(user_id))
	get_unread_msg = lambda user_id: client.get("/messages/unread/" + str(user_id)).get_json()

	message_text = "hello"
	print(f'\nSending a message: "{message_text}"...')
	message = send_msg(message_text)
	message_id = get_id(message)

	message2_text = "this is a message"
	message2 = send_msg(message2_text)
	message2_id = get_id(message2)

	vincent = add_user("Vincent")
	vincent_id = get_id(vincent)
	print("\nAdding user Vincent...")
	print("User id: ", vincent_id)

	print("Reading message")
	read_msg(message_id, vincent_id)

	print("Amount of messages should be 2:")
	print("All messages: ", get_msgs())
	assert len(get_msgs()) == 2

	print("Unread messages should be 1, since one of them is read:")
	print("Unread messages: ", get_unread_msg(vincent_id))
	assert len(get_unread_msg(vincent_id)) == 1

# pytest --cov-report html --cov=server test.py
