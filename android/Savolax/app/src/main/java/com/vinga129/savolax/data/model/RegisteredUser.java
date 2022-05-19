package com.vinga129.savolax.data.model;

public class RegisteredUser {

    private String username;
    private String email;
    private String customer_number;

    public RegisteredUser(String username, String email, String customer_number) {
        this.username = username;
        this.email = email;
        this.customer_number = customer_number;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomer_number() {
        return customer_number;
    }
}
