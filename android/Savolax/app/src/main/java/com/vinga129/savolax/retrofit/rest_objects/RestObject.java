package com.vinga129.savolax.retrofit.rest_objects;

import androidx.annotation.Nullable;

public abstract class RestObject {
    @Nullable
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
