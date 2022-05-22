package com.vinga129.savolax.ui.retrofit.rest_objects;

public class Address {
    private groups.AddressTypes address_type;
    private String street;
    private String city;
    private String zip_code;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
}
