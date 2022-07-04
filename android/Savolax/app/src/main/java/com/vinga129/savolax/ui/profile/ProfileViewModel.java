package com.vinga129.savolax.ui.profile;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.retrofit.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.CustomerProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"FieldMayBeFinal"})
public class ProfileViewModel extends NetworkViewModel {
    private MutableLiveData<CustomerProfile> customerProfile = new MutableLiveData<>();

    public ProfileViewModel(Application application) {
        super(application);
        // loadData();
    }

    public void init(int id) {
        loadData(id);
    }

    public LiveData<CustomerProfile> getCustomerProfile() {
        return customerProfile;
    }

    /*private MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private MutableLiveData<String> imageUrl = new MutableLiveData<>();
    private MutableLiveData<String> businessName = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<List<MiniCustomer>> followers = new MutableLiveData<>();
    private MutableLiveData<List<MiniCustomer>> following = new MutableLiveData<>();
    private MutableLiveData<String> bio = new MutableLiveData<>();

    public ProfileViewModel(int id, Application application) {
        super(application);
        loadData();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public LiveData<String> getBusinessName() {
        return businessName;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<List<MiniCustomer>> getFollowers() {
        return followers;
    }

    public LiveData<List<MiniCustomer>> getFollowing() {
        return following;
    }

    public LiveData<String> getBio() {
        return bio;
    }*/

    public void loadData(int id) {
        Call<CustomerProfile> customerProfileCall = restAPI.getCustomerProfile(id);
        customerProfileCall.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(@NonNull Call<CustomerProfile> call, @NonNull Response<CustomerProfile> response) {
                // CustomerProfile customerProfile = response.body();

                customerProfile.setValue(response.body());

                /*if (customerProfile != null) {
                    /*posts.setValue(customerProfile.getPosts());
                    imageUrl.setValue(customerProfile.getImageUrl());
                    businessName.setValue(customerProfile.getBusinessName());
                    username.setValue(customerProfile.getUsername());
                    followers.setValue(customerProfile.getFollowers());
                    following.setValue(customerProfile.getFollowing());
                    bio.setValue(customerProfile.getBio());*/
                //}
            }

            @Override
            public void onFailure(@NonNull Call<CustomerProfile> call, @NonNull Throwable t) {
                System.out.println("fail");
            }
        });
    }

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.get().load(url).error(error).into(view);
        if (url == null) {
            view.setImageDrawable(error);
        }
    }

    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView, PostPreviewsRecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("submitList")
    public static <T> void submitList(RecyclerView recyclerView, List<T> items) {
        BaseRecyclerAdapter adapter = (BaseRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null)
            adapter.updateData((List<Object>)items);
    }

    @BindingAdapter("loadBackgroundUrl")
    public static void setBackground(LinearLayout linearLayout, String imageUrl) {
        if (imageUrl != null) {
            ImageView img = new ImageView(linearLayout.getContext());
            Picasso.get().load(imageUrl).into(img, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    linearLayout.setBackground(img.getDrawable());
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
}