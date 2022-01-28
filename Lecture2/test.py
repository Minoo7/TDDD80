import os
import tempfile
import pytest
#from server import app
import server


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

def add_user(client):
    rv = client.post("/user/" + "Vincent")
    print(rv.status_code)


# def test_add_with_post(client):
#    rv = client.post("/add_with_post", json={'user': 'Anna', 'email': 'anna@liu.se'})
#    assert 200 == rv.status_code

#pytest --cov-report html --cov=server test.py