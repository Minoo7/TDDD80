import json

import requests

root = "http://localhost:5080/"


def check_json(response):
    if response.headers.get('Content-Type').startswith('application/json'):
        return response.json()
    return response.text + ", " + str(response.status_code)


def get_msgs():
    return check_json(requests.get(root + "messages"))


def send_msg(msg):
    return check_json(requests.post(root + "messages", json=msg))


def get_msg(msg_id):
    return requests.get(root + "messages/" + str(msg_id)).json()


def delete_msg(msg_id):
    return requests.delete(root + "messages/" + str(msg_id))


def read_msg(msg_id, user_id):
    return requests.post(root + "messages/" + str(msg_id) + "/read/" + str(user_id))


def get_unread_msgs(user_id):
    return requests.get(root + "messages/unread/" + str(user_id)).json()


def add_user(name):
    return check_json(requests.post(root + "user/" + name))


def reset():
    return requests.get(root + "reset")


if __name__ == "__main__":

    # Send message
    first_message = 'Hello this is a message!'
    first_message_id = send_msg({'message': first_message})['id']

    # Get message by id
    response_get = get_msg(first_message_id)
    assert response_get  # It exists
    print("Get message by id: ", response_get)
    assert response_get['message'] == first_message

    # Read message by id
    # ...sending new message:
    message_to_read = {"message": "This message should be read!"}
    message_to_read_id = int(send_msg(message_to_read)['id'])
    read_msg(message_to_read_id, 'Vincent')
    print(get_msg(message_to_read_id))
    assert get_msg(message_to_read_id)['readBy'] == ['Vincent']

    # Get all unread messages by user id
    unread_msgs = get_unread_msgs('Vincent')
    # ...should only be 'message' since 'message_to_read' is read
    assert unread_msgs[0]['message'] == first_message

    # Delete message by id
    # ...deleting both msgs
    assert get_msgs()  # amount of messages in database > 0
    delete_msg(first_message_id)  # delete first message
    assert first_message_id not in get_msgs()  # confirm the deletion was successful
    assert len(get_msgs()) == 1  # amount of messages should be 1

    # ...delete second message:
    delete_msg(message_to_read_id)
    assert not get_msgs()  # list of messages should be empty



    # print(add_user("test"))
    #print(requests.get(root + "user/" + "user").headers)
    # print(send_msg({"message": "this is a message"}).json())
    # print(delete_msg(1))
    # print(get_msg(1))
    # print(add_user("Vincent"))
    # print(add_user("Alex"))
    # print(send_msg({"message": "another msg"}).json())
    # print(get_msgs())
    # print(read_msg(1, 1))
    # print(read_msg(2, 2))
    # print(read_msg(2, 1))
    # print(get_msgs())
    # print(get_unread_msgs(1))
