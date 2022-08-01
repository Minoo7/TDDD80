package com.vinga129.savolax.retrofit.rest_objects;

import java.io.Serializable;

@SuppressWarnings("unused")
public class MiniCustomer extends RestObject implements Serializable {
    private String username;
    private String imageUrl;

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
