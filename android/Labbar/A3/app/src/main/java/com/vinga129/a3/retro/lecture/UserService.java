package com.vinga129.a3.retro.lecture;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("~andla63/json/test.json")
    Call<RetroUser> getUser();
}