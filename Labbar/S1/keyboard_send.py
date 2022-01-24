import requests
import keyboard
import time

if __name__ == "__main__":
    running = True
    global saved
    saved = 0
    while running:
        if keyboard.is_pressed('1'):  # POST MSG
            msg = {"message": "Hello this is a message"}
            value = requests.post("http://127.0.0.1:5087/messages", json=msg)

            saved = value.json()['id']

            print("\nreturned: ", value.json())

            time.sleep(1) # avoid double action
        if keyboard.is_pressed('2'):  # GET MSG
            url = "http://127.0.0.1:5087/messages/" + str(saved)
            value = requests.get(url)
            print("\nreturned: ", value.json())

            time.sleep(1)

        if keyboard.is_pressed('3'):  # DELETE MSG
            url = "http://127.0.0.1:5087/messages/" + str(saved)
            value = requests.delete(url)
            print("\ndeleted")

            time.sleep(1) # avoid double action

        if keyboard.is_pressed('4'):  # READ MSG
            url = "http://127.0.0.1:5087/messages/" + str(saved) + "/read/Vincent"
            value = requests.post(url)
            print("\nread")

            time.sleep(1)  # avoid double action

        if keyboard.is_pressed('5'):  # GET ALL MSG
            url = "http://127.0.0.1:5087/messages"
            value = requests.get(url)
            print("\nreturned: ", value.json())

            time.sleep(1) # avoid double action

        if keyboard.is_pressed('6'):  # GET UNREAD MSG
            url = "http://127.0.0.1:5087/messages/unread/" + "Vincent"
            value = requests.get(url)
            print("\nreturned: ", value.json())

            time.sleep(1)  # avoid double action

        if keyboard.is_pressed('x'):
            print("Exiting...")
            running = False