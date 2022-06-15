package com.vinga129.savolax.ui.add_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.ui.retrofit.rest_objects.groups;

public class AddPostViewModel extends ViewModel {

    private MutableLiveData<String[]> post_types = new MutableLiveData<>(groups.enumToStrings(groups.PostTypes.values(),
            groups.PostTypes::name));

    public LiveData<String[]> getPostTypes() {
        return post_types;
    }
}