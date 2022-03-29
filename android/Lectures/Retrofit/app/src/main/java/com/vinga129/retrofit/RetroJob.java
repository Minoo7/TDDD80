package com.vinga129.retrofit;
import com.google.gson.annotations.SerializedName;

public class RetroJob {
    @SerializedName("title")
    private  String title;
    @SerializedName("employer")
    private String employer;

    public RetroJob(String title, String empolyer){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
}