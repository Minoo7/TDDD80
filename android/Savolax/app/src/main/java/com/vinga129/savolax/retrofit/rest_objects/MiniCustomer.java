package com.vinga129.savolax.retrofit.rest_objects;

import java.io.Serializable;

public class MiniCustomer implements Serializable {
    private int id;
    private String username;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
