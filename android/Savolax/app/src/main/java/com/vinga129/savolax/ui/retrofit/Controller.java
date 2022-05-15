package com.vinga129.savolax.ui.retrofit;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.NetworkReceiver;
import com.vinga129.savolax.ui.profile.ProfileViewModel;
import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Controller {
    private static Retrofit retrofit;
    public static RestAPI restAPI;
    // private static final String BASE_URL = "https://tddd80-2022.herokuapp.com/";
    private static final String BASE_URL = "http://10.0.0.7:5080/";
    private static Controller INSTANCE = null;

    public static Controller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }

    private Controller() {
        retrofit = getRetrofitInstance();
        restAPI = getRestAPI();
    }

    public static RestAPI getRestAPI() {
        if (restAPI == null) {
            restAPI = getRetrofitInstance().create(RestAPI.class);
        }
        return restAPI;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            /*retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();*/
        }
        return retrofit;
    }

    public <T> void doNetwork(Call<T> call, Callback<T> callBack) {
        call.enqueue(callBack);
    }

    /*public CustomerProfile getCustomerProfile(String id) {

        restAPI.getCustomerProfile(id).enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(@NonNull Call<CustomerProfile> call, @NonNull Response<CustomerProfile> response) {

            }

            @Override
            public void onFailure(@NonNull Call<CustomerProfile> call, @NonNull Throwable t) {

            }
        });
    }*/

    public <T> void doNetworkCall(NetworkReceiver receiver, Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                receiver.onNetworkReceived(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            }
        });
    }
}
