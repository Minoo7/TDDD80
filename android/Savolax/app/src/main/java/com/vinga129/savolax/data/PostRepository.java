package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.Completable;

public class PostRepository {
    private static volatile PostRepository instance;

    private PostRepository() {}

    public static PostRepository getInstance() {
        if (instance == null)
            instance = new PostRepository();
        return instance;
    }

    public Completable uploadPost(Post post) {
        return PostDataSource.uploadPost(post);
    }

    public static class PostDataSource {
        public static Completable uploadPost(Post post) {
            return Controller.getInstance().getRestAPI().uploadPost(post);
        }
    }
}
