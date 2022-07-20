package com.vinga129.savolax.ui.profile;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.base.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.CustomerProfile;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class ProfileViewModel extends NetworkViewModel {

    private final int customerId;

    private final ObservableField<CustomerProfile> customerProfile = new ObservableField<>();

    public ObservableField<CustomerProfile> getCustomerProfile() {
        restAPI.getCustomerProfile(customerId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this.customerProfile::set).subscribe();
        return customerProfile;
    }

    public ProfileViewModel(int customerId) {
        this.customerId = customerId;
    }

    /*public void loadData(int customerId) {
        restAPI.getCustomerProfile(customerId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this.customerProfile::set).subscribe();
    }*/

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @BindingAdapter("submitList")
    public static <T> void submitList(RecyclerView recyclerView, List<T> items) {
        if (recyclerView.getAdapter() instanceof BaseRecyclerAdapter && recyclerView.getAdapter() != null)
            ((BaseRecyclerAdapter) recyclerView.getAdapter()).updateData(items);
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