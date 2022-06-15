package com.vinga129.savolax.ui.register;

import androidx.annotation.Nullable;
import com.vinga129.savolax.ui.address.AddressUserView;
import java.util.List;
import java.util.Map;

public class RegisterResult {
    @Nullable
    private RegisteredUserView success;
    @Nullable
    private Map<String, List<String>> errorMap;

    RegisterResult(@Nullable Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
    }

    RegisterResult(@Nullable RegisteredUserView success) {
        this.success = success;
    }

    @Nullable
    RegisteredUserView getSuccess() {
        return success;
    }

    @Nullable
    Map<String, List<String>> getErrorMap() {
        return errorMap;
    }
}
