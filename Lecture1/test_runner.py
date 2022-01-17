import requests

#for x in range(10):
r = requests.get("http://127.0.0.1:5080/add/anders/anders@liu.se") #+ str(x)
print(r.json)
data = requests.get("http://127.0.0.1:5080/get/anders")
print(data.json())

by_post = requests.post("http://127.0.0.1:5080/add_with_post", json={"user": "Anders", "email": "anders@gmail.com"})

print(by_post)
