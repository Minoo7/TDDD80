package com.vinga129.savolax.ui.address;

import com.vinga129.savolax.data.UserView;

public class AddressUserView implements UserView {
    private String username;


    AddressUserView(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
