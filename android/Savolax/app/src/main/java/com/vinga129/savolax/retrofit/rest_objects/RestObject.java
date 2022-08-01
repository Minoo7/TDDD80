package com.vinga129.savolax.retrofit.rest_objects;

import androidx.annotation.Nullable;

public abstract class RestObject {
    @Nullable
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
