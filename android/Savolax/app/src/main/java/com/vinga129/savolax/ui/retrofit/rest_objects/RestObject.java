package com.vinga129.savolax.ui.retrofit.rest_objects;

public abstract class RestObject {
    protected int id;

    /*public RestObject(int id) {
        this.id = id;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
