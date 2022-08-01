package com.vinga129.savolax.ui.add_post;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vinga129.savolax.data.AddImageRepository;
import com.vinga129.savolax.data.PostRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AddPostViewModel extends AndroidViewModel {

    private final MutableLiveData<ResultHolder<?>> addPostResult = new MutableLiveData<>();

    public AddPostViewModel(@NonNull final Application application) {
        super(application);
    }

    public LiveData<ResultHolder<?>> getAddPostResult() {
        return addPostResult;
    }

    private final CompletableObserver onAddPost = new CompletableObserver() {
        @Override
        public void onSubscribe(final Disposable d) {}

        @Override
        public void onComplete() {
            addPostResult.setValue(new ResultHolder<>());
        }

        @Override
        public void onError(final Throwable e) {
            if (e instanceof HttpException) {
                addPostResult.setValue(parseHttpError((HttpException) e));
            }
        }
    };

    public void addPost(Post post) {
        // Execute completable and handle response
        PostRepository.getInstance().uploadPost(post).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(onAddPost);
    }

    @SuppressWarnings("unchecked")
    public void addPostWithImage(Bitmap bitmap, Post post) {
        // Upload image from bitmap
        Single<Result> addImage = AddImageRepository.getInstance()
                .uploadImage(getApplication(), bitmap);

        // Use uploaded image and add to post
        // Upload post.
        Completable addPost = addImage
                .flatMapCompletable(integerResult -> {
                    int image_id = ((Success<Integer>) integerResult).getData();
                    post.setImage_id(image_id);
                    return PostRepository.getInstance().uploadPost(post);
                });

        addPost.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(onAddPost);
    }
}