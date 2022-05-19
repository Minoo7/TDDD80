package com.vinga129.savolax.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    // change class from loginformstate to generate strings for errors by doing jsonhandling by key value after restapi request...

    private boolean isDataValid;
    @Nullable
    private String usernameError;
    @Nullable
    private String passwordError;
    @Nullable
    private String first_name;
    @Nullable
    private String last_name;
    @Nullable
    private String email;

    RegisterFormState(@Nullable String usernameError, @Nullable String passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    String getUsernameError() {
        return usernameError;
    }

    @Nullable
    String getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    // TODO: more
}
