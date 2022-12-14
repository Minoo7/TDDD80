import requests


def get_msgs():
    return requests.get("http://127.0.0.1:5087/messages").json()


def send_msg(msg):
    return requests.post("http://127.0.0.1:5087/messages", json=msg).json()


def get_msg(msg_id):
    return requests.get("http://127.0.0.1:5087/messages/" + msg_id).json()


def delete_msg(msg_id):
    return requests.delete("http://127.0.0.1:5087/messages/" + msg_id)


def read_msg(msg_id, user_id):
    return requests.post("http://127.0.0.1:5087/messages/" + msg_id + "/read/" + user_id)


def get_unread_msgs(user_id):
    return requests.get("http://127.0.0.1:5087/messages/unread/" + user_id).json()


if __name__ == "__main__":

    # Send message
    first_message = 'Hello this is a message!'
    first_message_id = send_msg({'message': first_message})['id']
    print("first_message_id: ", first_message_id)

    # Get message by id
    response_get = get_msg(first_message_id)
    print(response_get)
    assert response_get  # It exists
    assert response_get['message'] == first_message

    # Read message by id
    # ...sending new message:
    message_to_read = {"message": "This message should be read!"}
    message_to_read_id = send_msg(message_to_read)['id']
    read_msg(message_to_read_id, 'Vincent')
    assert get_msg(message_to_read_id)['readBy'] == ['Vincent']

    # Get all unread messages by user id
    unread_msgs = get_unread_msgs('Vincent')
    print("Messages not read by user: ", unread_msgs)
    # ...should only be 'message' since 'message_to_read' is read
    assert unread_msgs[0]['message'] == first_message

    # Delete message by id
    # ...deleting both msgs
    assert get_msgs()  # amount of messages in database > 0
    response = delete_msg(first_message_id)  # delete first message
    assert response.status_code == 200
    assert first_message_id not in get_msgs()  # confirm the deletion was successful
    print("First message should not be in all messages: ", get_msgs())
    assert len(get_msgs()) == 1  # amount of messages should be 1

    # ...delete second message:
    delete_msg(message_to_read_id)
    print("All messages should be empty: allmsgs: ", get_msgs())
    assert not get_msgs()  # list of messages should be empty

    # Error checking:
    print("Trying to get first message in list should return 404:")
    print("'Trying to get msg in list...': return status = ", requests.get("http://127.0.0.1:5087/messages/" + first_message_id).status_code)
    assert requests.get("http://127.0.0.1:5087/messages/" + first_message_id).status_code == 404
    print(requests.get("http://127.0.0.1:5087/messages/" + first_message_id).status_code == 404)

    print("\nTrying to read msg that doesnt exist:")
    read = requests.post("http://127.0.0.1:5087/messages/" + 'not_exist' + "/read/" + "Vincent")
    print("Response: ", read)
    assert read.status_code == 404

    print("\nTrying to delete msg that doesnt exist:")
    dele = requests.delete("http://127.0.0.1:5087/messages/" + "no")
    print("Response: ", dele)
    assert dele.status_code == 404
