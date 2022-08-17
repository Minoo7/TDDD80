package com.vinga129.savolax.ui.login;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUserView {

    private int id;
    private boolean first_login;
    private String token;

    public int getId() {
        return id;
    }

    public boolean getFirstLogin() {
        return first_login;
    }

    public String getToken() {
        return token;
    }
}