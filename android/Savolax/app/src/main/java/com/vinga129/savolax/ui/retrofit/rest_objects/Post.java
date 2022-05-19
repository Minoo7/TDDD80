package com.vinga129.savolax.ui.retrofit.rest_objects;

import java.util.List;

public class Post {
    private int id;
    private int customer_id;
    private String image_url;
    private String title;
    private String content;
    private groups.PostTypes type;
    private String created_at;
    private List<Like> likes;
    private List<Comment> comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getImageUrl() {
        if (image_url != null)
            return "https://picsum.photos/536/354";
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

    public groups.PostTypes getType() {
        return type;
    }

    public void setType(groups.PostTypes type) {
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
