package com.vinga129.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("~andla63/json/test.json")
    Call<RetroUser> getUser();
}