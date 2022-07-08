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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"FieldMayBeFinal"})
public class ProfileViewModel extends NetworkViewModel {

    private MutableLiveData<CustomerProfile> customerProfile = new MutableLiveData<>();

    public ProfileViewModel(int customerId) {
        loadData(customerId);
    }

    public LiveData<CustomerProfile> getCustomerProfile() {
        return customerProfile;
    }

    public void loadData(int customerId) {
        restAPI.getCustomerProfile(customerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<CustomerProfile>() {
                            @Override
                            public void onSuccess(final CustomerProfile value) {
                                customerProfile.setValue(value);
                            }

                            @Override
                            public void onError(final Throwable e) {

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
            adapter.updateData((List<Object>) items);
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