package com.vinga129.savolax.retrofit;

import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.login.LoggedInUserView;
import io.reactivex.Single;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NoAuthAPI {

    @Headers("Content-Type: application/json")
    @POST("login")
    Single<LoggedInUserView> login(@Body Map<String, String> _login);

    @Headers("Content-Type: application/json")
    @POST("customers")
    Single<Map<String, String>> createCustomer(@Body Customer customer);

    @POST("login/token")
    Single<LoggedInUserView> loginWithToken(@Header("Authorization") String token);
}
