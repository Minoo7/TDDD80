package com.vinga129.savolax.ui.address;

import com.vinga129.savolax.ui.retrofit.rest_objects.groups;

public class AddressUserView {
    private String address_type;
    private String street;

    public AddressUserView(String address_type, String street) {
        this.address_type = address_type;
        this.street = street;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
