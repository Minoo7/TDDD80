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
    #return check_json(requests.post(root + "messages", json=msg))
    return requests.post(root + "messages", json=msg)


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
    user_id = add_user("VInc")['userId']
    print(user_id)
    print(send_msg({"message": "this is a message"}).json())
    print(read_msg(1, user_id))


    # print(add_user("test"))
    #print(requests.get(root + "user/" + "user").headers)
    # print(send_msg({"message": "this is a message"}).json())
    # print(delete_msg(1))
    # print(get_msg(1))
    # print(add_user("Vincent"))
    # print(add_user("Alex"))
    # print(send_msg({"message": "another msg"}).json())
    # print(get_msgs())
    # v
    # print(read_msg(2, 2))
    # print(read_msg(2, 1))
    # print(get_msgs())
    # print(get_unread_msgs(1))
