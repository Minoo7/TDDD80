package com.vinga129.savolax.retrofit.rest_objects;

public class RequestResult<T> {
    T data;
    int code;

    public RequestResult(final T data, final int code) {
        this.data = data;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
