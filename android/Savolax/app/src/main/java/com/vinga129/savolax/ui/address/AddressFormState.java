package com.vinga129.savolax.ui.address;

import androidx.annotation.Nullable;

public class AddressFormState {

    private boolean isDataValid;
    @Nullable
    private String streetError;

    AddressFormState(@Nullable String streetError) {
        this.streetError = streetError;
        this.isDataValid = false;
    }

    AddressFormState(boolean isDataValid) {
        this.streetError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    String getStreetError() {
        return streetError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    // TODO: more
}
