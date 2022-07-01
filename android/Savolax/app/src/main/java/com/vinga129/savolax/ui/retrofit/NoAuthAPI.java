package com.vinga129.savolax.ui.retrofit;

import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import io.reactivex.Single;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NoAuthAPI {

    @Headers("Content-Type: application/json")
    @POST("login")
    Single<Map<String, String>> login(@Body Map<String, String> _login);

    @Headers("Content-Type: application/json")
    @POST("customers")
    Single<Map<String, String>> createCustomer(@Body Customer customer);
}
