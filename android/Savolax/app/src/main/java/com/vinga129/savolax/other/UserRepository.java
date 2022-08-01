package com.vinga129.savolax.other;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class UserRepository {
    public static UserRepository INSTANCE;

    private static RestAPI restAPI;
    private User user;

    private UserRepository() {
        restAPI = Controller.getInstance().getRestAPI();
    }

    public static UserRepository getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new UserRepository();
        return INSTANCE;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateData(String apiKey) {
        restAPI.getUpdated().enqueue(new Callback<User>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null)
                    ;
                    //user.postValue(response.body());
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return user.getId();
    }
}
