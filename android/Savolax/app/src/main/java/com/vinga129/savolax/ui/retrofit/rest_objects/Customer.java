package com.vinga129.savolax.ui.retrofit.rest_objects;

import androidx.annotation.Nullable;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.BusinessTypes;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.Genders;

public class Customer {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private groups.Genders gender;
    private String phone_number;
    private groups.BusinessTypes businessType;
    private String organization_number;
    private String business_name;
    private String bio;

    public Customer(final String username, final String password, final String first_name, final String last_name,
            final String email,
            @Nullable final Genders gender, final String phone_number,
            final BusinessTypes businessType, final String organization_number, final String business_name,
            @Nullable final String bio) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.phone_number = phone_number;
        this.businessType = businessType;
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

    public groups.Genders getGender() {
        return gender;
    }

    public void setGender(groups.Genders gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public groups.BusinessTypes getBusinessType() {
        return businessType;
    }

    public void setBusinessType(groups.BusinessTypes businessType) {
        this.businessType = businessType;
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
}
