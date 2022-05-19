package com.vinga129.savolax.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.RestAPI;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class UserRepository {
    public static UserRepository INSTANCE;

    private static RestAPI restAPI;
    private MutableLiveData<User> userLiveData;

    private UserRepository() {
        restAPI = Controller.getInstance().getRestAPI();
        userLiveData = new MutableLiveData<>(new User(1));
    }

    public static UserRepository getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new UserRepository();
        return INSTANCE;
    }

    public void updateData(String apiKey) {
        restAPI.getUpdated().enqueue(new Callback<User>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null)
                    userLiveData.postValue(response.body());
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public int getId() {
        return Objects.requireNonNull(getUserLiveData().getValue()).getId();
    }
}
