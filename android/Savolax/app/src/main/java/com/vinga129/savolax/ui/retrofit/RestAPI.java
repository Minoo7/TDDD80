package com.vinga129.savolax.ui.retrofit;

import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("customers/{id}/profile")
    Call<CustomerProfile> getCustomerProfile(@Path("id") String id); // String?

    @GET("1")
    Call<String> getTest();
}
