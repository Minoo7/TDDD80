package com.vinga129.savolax.data;

import androidx.annotation.Nullable;
import com.vinga129.savolax.data.Result.Error;

public class ResultHolder<T> {

    protected boolean succeeded = false;
    @Nullable
    protected T success;
    @Nullable
    protected Error error;

    public ResultHolder() {
        this.succeeded = true;
    }

    public boolean isSuccess() {
        return succeeded;
    }

    public ResultHolder(@Nullable T success) {
        this.success = success;
    }

    @Nullable
    public T getSuccess() {
        return success;
    }

    public ResultHolder(@Nullable Error error) {
        this.error = error;
    }

    @Nullable
    public Error getError() {
        return error;
    }

}
