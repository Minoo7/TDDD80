package com.vinga129.savolax.data.model;

import com.vinga129.savolax.ui.retrofit.rest_objects.groups;

public class AddressUser {
    private groups.AddressTypes address_type;
    private String street;

    public AddressUser(groups.AddressTypes address_type, String street) {
        this.address_type = address_type;
        this.street = street;
    }

    public groups.AddressTypes getAddress_type() {
        return address_type;
    }

    public void setAddress_type(groups.AddressTypes address_type) {
        this.address_type = address_type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
