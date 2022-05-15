package com.vinga129.savolax.ui.post;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vinga129.savolax.ui.retrofit.NetworkViewModel;
import com.vinga129.savolax.ui.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"FieldMayBeFinal"})
public class PostViewModel extends NetworkViewModel {
    private MutableLiveData<Post> post = new MutableLiveData<>();
    private MutableLiveData<MiniCustomer> miniCustomer = new MutableLiveData<>();

    public PostViewModel(Application application) {
        super(application);
    }

    public LiveData<Post> getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post.setValue(post);
    }

    public LiveData<MiniCustomer> getMiniCustomer() {
        return miniCustomer;
    }

    public void setMiniCustomer(MiniCustomer customer) {
        this.miniCustomer.setValue(customer);
    }

    public void init(Post post) {
        this.post.setValue(post);
        Call<MiniCustomer> miniCustomerCall = restAPI.getCustomerAsMini(post.getId());
        miniCustomerCall.enqueue(new Callback<MiniCustomer>() {
            @Override
            public void onResponse(@NonNull Call<MiniCustomer> call, @NonNull Response<MiniCustomer> response) {
                miniCustomer.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MiniCustomer> call, @NonNull Throwable t) {
                System.out.println("ddd");
            }
        });
    }

    public void loadData() {
        /*Call<Post> postCall = restAPI.getPost(1);
        Call<CustomerProfile> customerProfileCall = restAPI.getCustomerProfile("1");
        customerProfileCall.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(@NonNull Call<CustomerProfile> call, @NonNull Response<CustomerProfile> response) {
                CustomerProfile customerProfile = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<CustomerProfile> call, @NonNull Throwable t) {

            }
        });*/
    }
}