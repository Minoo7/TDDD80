package com.vinga129.a3.retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface UserService {
    @GET("/grupper")
    Call<RetroGroup> getGroups();

    @GET("/medlemmar/{group}")
    Call<RetroUserList> getMembers(@Path("group") String group);

    //@GET("/grupper")
    //Call<List> listRepos(@Path("user") String user);
}