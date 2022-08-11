package com.vinga129.savolax.ui.home;

import android.util.Pair;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import com.vinga129.savolax.base.NetworkBaseObservable;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeViewModel extends NetworkBaseObservable {

    private final ObservableBoolean showSearch = new ObservableBoolean(false);
    private final ObservableField<List<Post>> posts = new ObservableField<>();
    private final ObservableBoolean showNoFollowingInfoText = new ObservableBoolean(false);

    public HomeViewModel(int customerId) {
        loadData(customerId);
    }

    public ObservableBoolean getShowSearch() {
        return showSearch;
    }

    public ObservableField<List<Post>> getPosts() {
        return posts;
    }

    public ObservableBoolean getShowNoFollowingInfoText() {
        return showNoFollowingInfoText;
    }

    public void setShowNoFollowingInfoText(boolean value) {
        if (Objects.requireNonNull(posts.get()).isEmpty())
            showNoFollowingInfoText.set(value);
    }

    private void loadData(int customerId) {
        // load all posts
        restAPI.getFeed(customerId)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable(),
                        Pair::new)
                .toList()
                .doOnSuccess(pairs -> posts.set(pairs.stream()
                        .map(pair -> pair.first.withCustomer(pair.second)).collect(Collectors.toList())))
                .doAfterSuccess(pairs -> {
                    if (pairs.isEmpty())
                        showNoFollowingInfoText.set(true);
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();


    }
}