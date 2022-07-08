package com.vinga129.savolax.custom;

import java.io.Serializable;

public class CustomNullableIntegerArgument implements Serializable  {
    private final Integer value;

    public CustomNullableIntegerArgument(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
