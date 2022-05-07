package com.vinga129.savolax.ui.retrofit.rest_objects;

import java.util.List;

public class CustomerProfile {
    private String username;
    private String business_name;
    private groups.BusinessTypes business_type;
    private List<MiniCustomer> followers;
    private List<MiniCustomer> following;
    private List<Post> posts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
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
