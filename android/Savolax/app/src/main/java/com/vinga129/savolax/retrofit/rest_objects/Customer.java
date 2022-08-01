package com.vinga129.savolax.retrofit.rest_objects;

import androidx.annotation.Nullable;

@SuppressWarnings("unused")
public class Customer extends RestObject {

    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String gender;
    private String phone_number;
    private String business_type;
    private String organization_number;
    private String business_name;
    private String bio;
    private String customer_number;
    private Integer image_id;

    public Customer(final String username, final String password, final String first_name, final String last_name,
            final String email,
            @Nullable final String gender, final String phone_number,
            final String business_type, final String organization_number, final String business_name,
            @Nullable final String bio) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.phone_number = phone_number;
        this.business_type = business_type;
        this.organization_number = organization_number;
        this.business_name = business_name;
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getOrganization_number() {
        return organization_number;
    }

    public void setOrganization_number(String organization_number) {
        this.organization_number = organization_number;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(final String customer_number) {
        this.customer_number = customer_number;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(final int image_id) {
        this.image_id = image_id;
    }
}
