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


def test_add_user(client):
	rv = client.post("/user/" + "Vincent")
	print("\nrv.status_code: ", rv)


# message_id = r.data.decode(encoding='utf-8')


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
	message = send_msg(message_text)

	message2_text = "this is a message"
	message2 = send_msg(message2_text)

	assert len(get_msgs()) == 2

	delete_msg(get_id(message))

	assert len(get_msgs()) == 1

def test_add_with_post(client):
	rv = client.post("/add_with_post", json={'user': 'Anna', 'email': 'anna@liu.se'})
# assert 200 == rv.status_code

# pytest --cov-report html --cov=server test.py
