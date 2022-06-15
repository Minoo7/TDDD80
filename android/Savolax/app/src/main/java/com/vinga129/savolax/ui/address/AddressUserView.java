package com.vinga129.savolax.ui.address;

import com.vinga129.savolax.data.UserView;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.AddressTypes;

public class AddressUserView implements UserView {
    private String username;
    private String address_type;


    AddressUserView(String username, AddressTypes address_type) {
        this.username = username;
        this.address_type = address_type;
    }

    String getUsername() {
        return username;
    }

    public String getAddress_type() {
        return address_type;
    }
}
