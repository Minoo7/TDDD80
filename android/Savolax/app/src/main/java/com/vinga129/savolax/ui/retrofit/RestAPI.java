package com.vinga129.savolax.ui.retrofit;

import com.vinga129.savolax.ui.User;
import com.vinga129.savolax.ui.products.ProductG;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;
import com.vinga129.savolax.ui.retrofit.rest_objects.Like;
import com.vinga129.savolax.ui.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestAPI {
    @POST("customers")
    Call<Map<String, String>> createCustomer(@Body Customer customer);

    @POST("login")
    Call<Map<String, String>> login(@Body Map<String, String> body);

    @GET("login/update")
    Call<User> getUpdated();

    @GET("customers/{id}/profile")
    Call<CustomerProfile> getCustomerProfile(@Path("id") int id); // String?

    @GET("customers/{id}/mini")
    Call<MiniCustomer> getCustomerAsMini(@Path("id") int id);

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @POST("posts/{id}/likes")
    Call<Map<String, String>> likePost(@Path("id") int id);

    @DELETE("posts/{id}/likes/{like_id}")
    Call<Map<String, String>> deleteLike(@Path("id") int id, @Path("like_id") int like_id);

    @GET("test")
    Call<ProductG> getTest();
}
