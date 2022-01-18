import requests
import keyboard
import time

if __name__ == "__main__":
    running = True
    global saved
    saved = 0
    while running:
        if keyboard.is_pressed('s'):
            #print('You pressed s Key!')
            msg = {"message": "Hello this is a message"}
            send = requests.post("http://127.0.0.1:5087/messages", json=msg)
            print("send: ", send)
            time.sleep(3) # avoid double action
        if keyboard.is_pressed('g'):
            ret = requests.get("http://127.0.0.1:5087/messages/test")
            print(f"ret: {ret}")
            time.sleep(3) # avoid double action
        if keyboard.is_pressed('x'):
            print("Exiting...")
            running = False