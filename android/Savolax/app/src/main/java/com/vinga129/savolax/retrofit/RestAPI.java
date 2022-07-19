package com.vinga129.savolax.retrofit;

import com.vinga129.savolax.retrofit.rest_objects.Comment;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.later.User;
import com.vinga129.savolax.ui.products.ProductG;
import com.vinga129.savolax.retrofit.rest_objects.Address;
import com.vinga129.savolax.retrofit.rest_objects.CustomerProfile;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;

import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestAPI {
    /*@POST("login")
    Call<Map<String, String>> login(@Body Map<String, String> body);*/

    @GET("login/update")
    Call<User> getUpdated();

    @PUT("customers/{id}")
    Completable updateCustomer(@Path("id") int id, @Body Customer customer);

    @POST("customers/{id}/addresses")
    Single<Map<String, String>> addAddress(@Path("id") int id, @Body Address address);

    @GET("customers/{id}/profile")
    Single<CustomerProfile> getCustomerProfile(@Path("id") int id); // String?

    @Multipart
    @POST("customers/{id}/images")
    Single<Map<String, String>> uploadImage(@Path("id") int id, @Part MultipartBody.Part file);
    //Single<Map<String, String>> uploadImage(@Path("id") int id, @Body MultipartBody body);

    @GET("customers/{id}/feed")
    Single<List<Post>> getFeed(@Path("id") int id);

    @GET("customers/{id}/mini")
    Single<MiniCustomer> getCustomerAsMini(@Path("id") int id);

    @POST("posts")
    Completable uploadPost(@Body Post post);

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @POST("posts/{id}/likes")
    Completable likePost(@Path("id") int id);

    @DELETE("posts/{id}/unlike")
    Completable deleteLike(@Path("id") int id);

    @GET("test")
    Call<ProductG> getTest();
}
