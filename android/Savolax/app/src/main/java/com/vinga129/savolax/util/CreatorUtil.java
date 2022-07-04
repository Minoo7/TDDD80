package com.vinga129.savolax.util;

import com.vinga129.savolax.retrofit.rest_objects.Like;

public class CreatorUtil {
    public static Like createLike(int post_id, int customer_id) {
        Like like = new Like();
        like.setCustomer_id(customer_id);
        like.setPost_id(post_id);
        return like;
    }
}
