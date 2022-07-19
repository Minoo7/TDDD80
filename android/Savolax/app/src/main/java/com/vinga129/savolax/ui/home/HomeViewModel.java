package com.vinga129.savolax.ui.home;

import android.util.Pair;
import androidx.databinding.ObservableField;
import com.vinga129.savolax.retrofit.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function22;

public class HomeViewModel extends NetworkViewModel {

    private final ObservableField<List<Post>> posts = new ObservableField<>();

    public ObservableField<List<Post>> getPosts() {
        return posts;
    }

    public void loadData(int customerId) {
        /*restAPI.getFeed(customerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<List<Post>>() {
                            @Override
                            public void onSuccess(final List<Post> value) {
                                posts.set(value);
                            }

                            @Override
                            public void onError(final Throwable e) {

                            }
                        });*/
        //restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).flatMapSingle(post ->
        //          restAPI.getCustomerAsMini(post.getCustomerId()).doOnSuccess(post::setCustomer)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith()
        //restAPI.getFeed(customerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flat

        //Observable d = restAPI.getFeed(customerId).flatMap(Observable::fromIterable);

        //restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable()).flatMap(new )

        /*restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(post -> restAPI.getCustomerAsMini(post.getCustomerId()).doOnSuccess(
                        post::setCustomer)).toList().doOnSuccess(posts::set).subscribe();*/

        //restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable()).

        //Observable<Single<MiniCustomer>> observable = restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable)
        //      .map(post -> restAPI.getCustomerAsMini(post.getCustomerId()));
        //.flatMapSingle(post -> restAPI.getCustomerAsMini(post.getCustomerId()))

        //Observable.mergeDelayError(observable)

        /*restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).subscribeOn(Schedulers.io())
                .observeOn(
                        AndroidSchedulers.mainThread()).subscribeWith(new BlockingBaseObserver<Post>() {
            @Override
            public void onNext(final Post value) {
                restAPI.getCustomerAsMini(value.getCustomerId()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                        new DisposableSingleObserver<MiniCustomer>() {
                            @Override
                            public void onSuccess(final MiniCustomer value) {
                                posts.get().add(value)
                            }

                            @Override
                            public void onError(final Throwable e) {

                            }
                        })
                posts.get().add()
            }

            @Override
            public void onError(final Throwable e) {

            }
        });*/

        /*restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).flatMap(
                new Function<Post, Observable<MiniCustomer>>() {
                    @Override
                    public Observable<MiniCustomer> apply(final Post post) throws Exception {
                        return restAPI.getCustomerAsMini(post.getCustomerId()).toObservable();
                    }
                }, new BiFunction<Post, MiniCustomer, Pair<Post, MiniCustomer>>() {
                    @Override
                    public Pair<Post, MiniCustomer> apply(final Post post, final MiniCustomer miniCustomer)
                            throws Exception {
                        return new Pair<>(post, miniCustomer);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                new BlockingBaseObserver<Pair<Post, MiniCustomer>>() {
                    @Override
                    public void onNext(final Pair<Post, MiniCustomer> value) {
                        Post post = value.first;
                        MiniCustomer customer = value.second;

                        post.setCustomer(customer);

                        Objects.requireNonNull(posts.get()).add(post);
                    }

                    @Override
                    public void onError(final Throwable e) {

                    }
                });*/

        /*restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable).flatMap(
                (Function<Post, Observable<MiniCustomer>>) post -> restAPI.getCustomerAsMini(post.getCustomerId())
                        .toObservable(),
                Pair::new).toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<List<Pair<Post, MiniCustomer>>>() {
                            @Override
                            public void onSuccess(final List<Pair<Post, MiniCustomer>> value) {

                            }

                            @Override
                            public void onError(final Throwable e) {

                            }
                        })*/

        /*restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable)
                .flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable(), Pair::new).toList()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(pairs -> {
                    List<Post> p = pairs.stream().map(pair -> {
                        pair.first.setCustomer(pair.second);
                        return pair.first;
                    }).collect(Collectors.toList());
                    this.posts.set(p);
                }).subscribe();*/

        restAPI.getFeed(customerId).flatMapObservable(Observable::fromIterable)
                .flatMap(post -> restAPI.getCustomerAsMini(post.getCustomerId()).toObservable(),
                        Pair::new)
                .toList()
                .doOnSuccess(pairs -> posts.set(pairs.stream()
                        .map(pair -> pair.first.withCustomer(pair.second)).collect(Collectors.toList())))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

        /*Observable.just("foo")
                .flatMap(new Function<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(final String s) throws Exception {
                        return Observable.range(1, 5);
                    }
                }, new BiFunction<String, Integer, Pair<String, Integer>>() {
                    @Override
                    public Pair<String, Integer> apply(final String s, final Integer i) throws Exception {
                        return new Pair<>(s, i);
                    }
                });

        Observable.just("foo")
                .flatMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String foo) {
                        return Observable.range(1, 5);
                    }
                }, new Func2<String, Integer, Pair<Integer, String>>() {
                    @Override
                    public Pair<Integer, String> call(String s, Integer i) {
                        return new Pair<>(i, s);
                    }
                })
                .subscribe(new Action1<Pair<Integer, String>>() {
                    @Override
                    public void call(Pair<Integer, String> pair) {
                        System.out.println("Result: " + pair.first + " Foo: " + pair.second);
                    }
                });*/

    }
}