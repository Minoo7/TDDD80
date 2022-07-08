package com.vinga129.savolax.retrofit.rest_objects;

import androidx.annotation.Nullable;
import java.util.List;

public class Post {
    @Nullable
    private Integer id;
    private Integer customer_id;
    private String image_url;
    private Integer image_id;
    private String title;
    private String content;
    private String type;
    private String created_at;
    private List<Like> likes;
    private List<Comment> comments;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(final int image_id) {
        this.image_id = image_id;
    }

    public int getId() {
        //return id;
        return 1;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // Help methods

    public boolean isLikedBy(int customer_id) {
        return likes.stream().anyMatch(mini -> mini.getId() == customer_id);
    }
}
