package com.vinga129.savolax.ui.address;

import androidx.annotation.Nullable;

import com.vinga129.savolax.ui.register.RegisteredUserView;

public class AddressResult {
    @Nullable
    private AddressUserView success;
    @Nullable
    private Integer error;

    AddressResult(@Nullable Integer error) {
        this.error = error;
    }

    AddressResult(@Nullable AddressUserView success) {
        this.success = success;
    }

    @Nullable
    AddressUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
