package com.vinga129.a3.retro;

import com.google.gson.annotations.SerializedName;

public class RetroGroup {
    @SerializedName("grupper")
    private String[] groups;

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }
}
