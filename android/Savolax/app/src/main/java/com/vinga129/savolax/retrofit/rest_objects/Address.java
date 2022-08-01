package com.vinga129.savolax.retrofit.rest_objects;

@SuppressWarnings("unused")
public class Address extends RestObject{
    private String address_type;
    private String street;
    private String city;
    private String zip_code;

    public String getAddress_type() {
        return address_type;
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
