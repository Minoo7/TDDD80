package com.vinga129.savolax.ui.post;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import com.vinga129.savolax.base.NetworkBaseObservable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PostViewModel extends NetworkBaseObservable {

    private final ObservableBoolean liked = new ObservableBoolean();
    private final ObservableInt likeAmount = new ObservableInt();

    public PostViewModel(int likeAmount) {
        this.likeAmount.set(likeAmount);
    }

    public ObservableBoolean getLiked() {
        return liked;
    }

    public ObservableInt getLikeAmount() {
        return likeAmount;
    }

    public void setLiked(boolean value) {
        liked.set(value);
    }

    private final CompletableObserver observer = new CompletableObserver() {
        @Override
        public void onSubscribe(final Disposable d) {
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(final Throwable e) {

        }
    };

    public void initLikeValue(boolean value, int postId) {
        liked.set(value);
        liked.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(final Observable sender, final int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                System.out.println("hejsan 123");
                System.out.println("value " + value);

                if (value) {
                    likeAmount.set(likeAmount.get() + 1);
                    restAPI.likePost(postId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
                }
                else {
                    likeAmount.set(likeAmount.get() - 1);
                    restAPI.deleteLike(postId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
                }
            }
        });
    }
}