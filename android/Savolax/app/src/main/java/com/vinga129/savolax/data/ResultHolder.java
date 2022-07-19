package com.vinga129.savolax.data;

import androidx.annotation.Nullable;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Error;
import java.util.List;
import java.util.Map;
import retrofit2.HttpException;

public class ResultHolder<T> {

    @Nullable
    protected boolean succeeded = false;
    @Nullable
    protected T success;
    @Nullable
    protected Error error;

    public ResultHolder() {
        this.succeeded = true;
    }

    @Nullable
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

    public Error getError() {
        return error;
    }

}
