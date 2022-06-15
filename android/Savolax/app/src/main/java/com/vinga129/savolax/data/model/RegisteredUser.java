package com.vinga129.savolax.data.model;

public class RegisteredUser {

    private final String username;
    private final String email;
    private final String customer_number;

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
