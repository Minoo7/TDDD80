package com.vinga129.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetroUser {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RetroJob getJob() {
        return job;
    }

    public void setJob(RetroJob job) {
        this.job = job;
    }

    public ArrayList<String> getKids() {
        return kids;
    }

    public void setKids(ArrayList<String> kids) {
        this.kids = kids;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("job")
    private RetroJob job;
    @SerializedName("kids")
    private ArrayList<String> kids;

}