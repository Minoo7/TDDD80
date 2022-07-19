package com.vinga129.savolax.ui.post;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.Observable.OnPropertyChangedCallback;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.retrofit.NetworkBaseObservable;
import com.vinga129.savolax.retrofit.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"FieldMayBeFinal"})
public class PostViewModel extends NetworkViewModel {

    private ObservableField<MiniCustomer> miniCustomer = new ObservableField<>();
    private ObservableBoolean liked = new ObservableBoolean();

    public ObservableField<MiniCustomer> getMiniCustomer() {
        return miniCustomer;
    }

    public ObservableBoolean getLiked() {
        return liked;
    }

    public void setMiniCustomer(MiniCustomer customer) {
        this.miniCustomer.set(customer);
    }

    public void setLiked(boolean value) {
        liked.set(value);
    }

    private CompletableObserver observer = new CompletableObserver() {
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

    public void loadMiniCustomer(int customerId) {
        restAPI.getCustomerAsMini(customerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<MiniCustomer>() {
                            @Override
                            public void onSuccess(final MiniCustomer value) {
                                miniCustomer.set(value);
                            }

                            @Override
                            public void onError(final Throwable e) {

                            }
                        });
    }

    public void initLikeValue(boolean value, int postId) {
        liked.set(value);
        liked.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(final Observable sender, final int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();

                if (value)
                    restAPI.likePost(postId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
                else
                    restAPI.deleteLike(postId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(observer);
            }
        });
    }
}