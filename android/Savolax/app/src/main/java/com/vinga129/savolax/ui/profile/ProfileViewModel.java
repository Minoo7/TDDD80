package com.vinga129.savolax.ui.profile;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import com.vinga129.savolax.base.NetworkBaseObservable;
import com.vinga129.savolax.retrofit.rest_objects.CustomerProfile;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;

public class ProfileViewModel extends NetworkBaseObservable {

    private final int customerId;

    public final ObservableBoolean following = new ObservableBoolean();
    public final ObservableField<CustomerProfile> customerProfile = new ObservableField<>();

    public ProfileViewModel(int customerId) {
        this.customerId = customerId;

        restAPI.getCustomerProfile(customerId).doOnSuccess(customerProfile -> {
            this.customerProfile.set(customerProfile);
            if (user.getId() != customerId)
                initFollowing(customerProfile.isFollowedBy(user.getId()));
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void initFollowing(boolean value) {
        following.set(value);
        following.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(final Observable sender, final int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value)
                    restAPI.follow(user.getId(), Collections.singletonMap("customer_id", customerId))
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                else
                    restAPI.unfollow(user.getId(), customerId)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        });
    }
}