package com.vinga129.savolax.base;

import androidx.annotation.Nullable;
import com.vinga129.savolax.ui.register.RegisteredUserView;
import java.util.List;
import java.util.Map;

public class ResultHolder<T> {
    @Nullable
    protected T success;
    @Nullable
    private Map<String, List<String>> errorMap;

    public ResultHolder(@Nullable Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
    }

    public ResultHolder(@Nullable T success) {
        this.success = success;
    }

    @Nullable
    public T getSuccess() {
        return success;
    }

    @Nullable
    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }
}
