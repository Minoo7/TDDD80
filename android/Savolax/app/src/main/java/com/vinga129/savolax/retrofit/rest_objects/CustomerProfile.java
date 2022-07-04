package com.vinga129.savolax.retrofit.rest_objects;

import java.util.List;

public class CustomerProfile {
    private String username;
    private String business_name;
    private groups.BusinessTypes business_type;
    private String bio;
    private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private List<MiniCustomer> followers;
    private List<MiniCustomer> following;
    private List<Post> posts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBusinessName() {
        return business_name;
    }

    public void setBusinessName(String business_name) {
        this.business_name = business_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public groups.BusinessTypes getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(groups.BusinessTypes business_type) {
        this.business_type = business_type;
    }

    public List<MiniCustomer> getFollowers() {
        return followers;
    }

    public void setFollowers(List<MiniCustomer> followers) {
        this.followers = followers;
    }

    public List<MiniCustomer> getFollowing() {
        return following;
    }

    public void setFollowing(List<MiniCustomer> following) {
        this.following = following;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
