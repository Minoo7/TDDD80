package com.vinga129.a3.retro;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface UserService {
    @GET("/grupper")
    Call<RetroGroup> getGroups();

    @GET
    Call<RetroUserList> getMembers(@Url String url);
}