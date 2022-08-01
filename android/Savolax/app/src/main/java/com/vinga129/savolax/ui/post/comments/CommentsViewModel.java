package com.vinga129.savolax.ui.post.comments;

import android.util.Pair;
import androidx.databinding.ObservableField;
import com.vinga129.savolax.base.NetworkBaseObservable;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;

public class CommentsViewModel extends NetworkBaseObservable {
    private final int postId;

    public final ObservableField<List<Comment>> comments = new ObservableField<>();

    public CommentsViewModel(int postId, List<Comment> comments) {
        this.postId = postId;
        loadComments(comments);
    }

    private void loadComments(List<Comment> comments) {
        Observable.fromIterable(comments)
                .flatMap(comment -> restAPI.getCustomerAsMini(comment.getCustomer_id()).toObservable(),
                        Pair::new)
                .toList()
                .doOnSuccess(pairs -> this.comments.set(pairs.stream()
                        .map(pair -> pair.first.withCustomer(pair.second)).collect(Collectors.toList())))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void addComment(Comment comment) {
        restAPI.addComment(postId, comment)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}