package com.vinga129.savolax.ui.home;

import android.util.Pair;
import androidx.databinding.ObservableField;
import com.vinga129.savolax.base.NetworkBaseObservable;
import com.vinga129.savolax.base.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel extends NetworkBaseObservable {

    private final ObservableField<List<Post>> posts = new ObservableField<>();

    public HomeViewModel(int customerId) {
        loadData(customerId);
    }

    public ObservableField<List<Post>> getPosts() {
        return posts;
    }

    public void loadData(int customerId) {
        restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable)
                .flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable(),
                        Pair::new)
                .toList()
                .doOnSuccess(pairs -> posts.set(pairs.stream()
                        .map(pair -> pair.first.withCustomer(pair.second)).collect(Collectors.toList())))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}