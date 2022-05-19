package com.vinga129.savolax.ui;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.ui.retrofit.rest_objects.Like;

import java.util.List;

public class ActiveCustomerViewModel extends ViewModel {
    private final MutableLiveData<Integer> id = new MutableLiveData<>();
    private final MutableLiveData<List<Like>> likes = new MutableLiveData<>();

    public LiveData<Integer> getId() {
        return id;
    }

    public void setId(int id) {
        this.id.setValue(id);
    }

    public LiveData<List<Like>> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes.setValue(likes);
    }




    /*private final MutableLiveData<String> username = new MutableLiveData<>();

    public final void setProfilePicture(Bitmap bitmap) {
        this.profilePicture.postValue(bitmap);
    }

    public final LiveData<Bitmap> getProfilePicture() {
        return this.profilePicture;
    }

    public final Bitmap getProfilePictureValue() {
        return this.profilePicture.getValue();
    }

    public final void setUsername(String username) {
        this.username.setValue(username);
    }

    public final String getUsername() {
        return this.username.getValue();
    }*/
}
