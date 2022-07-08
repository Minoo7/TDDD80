package com.vinga129.savolax.data;

import androidx.annotation.Nullable;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Error;
import java.util.List;
import java.util.Map;
import retrofit2.HttpException;

public class ResultHolder<T> {

    @Nullable
    protected T success;
    @Nullable
    protected Error error;
    /*@Nullable
    private Map<String, List<String>> errorMap;

    @Nullable
    private int errorCode;
    @Nullable
    private String errorMessage;

    public ResultHolder(@Nullable Map<String, List<String>> errorMap) {
        this.errorMap = errorMap;
    }

    /*public ResultHolder(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }*/

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

    /*@Nullable
    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }

    public String getError() {
        return (errorCode != 0) ? errorCode + ": " + errorMessage : null;
    }*/
}
