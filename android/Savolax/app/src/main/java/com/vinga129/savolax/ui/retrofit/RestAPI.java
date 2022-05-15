package com.vinga129.savolax.ui.retrofit;

import com.vinga129.savolax.ui.products.ProductG;
import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;
import com.vinga129.savolax.ui.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("customers/{id}/profile")
    Call<CustomerProfile> getCustomerProfile(@Path("id") int id); // String?

    @GET("customers/{id}/mini")
    Call<MiniCustomer> getCustomerAsMini(@Path("id") int id);

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @GET("test")
    Call<ProductG> getTest();
}
