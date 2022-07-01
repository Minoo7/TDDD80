package com.vinga129.savolax.ui.login;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUserView {

    private final int userId;
    private final String displayName;
    private final boolean first_login;
    private final String JWT_KEY;

    public LoggedInUserView(String userId, String displayName, String first_login, String JWT_KEY) {
        this.userId = Integer.parseInt(userId);
        this.displayName = displayName;
        this.first_login = Boolean.parseBoolean(first_login);
        this.JWT_KEY = JWT_KEY;
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean getFirstLogin() {
        return first_login;
    }

    public String getJWT_KEY() {
        return JWT_KEY;
    }
}