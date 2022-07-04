package com.vinga129.savolax.ui.add_post;

import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.retrofit.rest_objects.groups;

public class AddPostViewModel extends ViewModel {

    private MutableLiveData<String[]> post_types = new MutableLiveData<>(
            groups.enumToStrings(groups.PostTypes.values(),
                    groups.PostTypes::name));

    public LiveData<String[]> getPostTypes() {
        return post_types;
    }

    private final MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();

    public void setCapturedImage(Bitmap item) {
        capturedImage.setValue(item);
    }

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }

    private final MutableLiveData<Integer> addPhotoVisibility = new MutableLiveData<>(View.VISIBLE);

    public LiveData<Integer> getAddPhotoVisibility() {
        return addPhotoVisibility;
    }

    private final MutableLiveData<Integer> imageResultVisibility = new MutableLiveData<>(View.GONE);

    public LiveData<Integer> getImageResultVisibility() {
        return imageResultVisibility;
    }

    public void showImageResult() {
        addPhotoVisibility.setValue(View.GONE);
        imageResultVisibility.setValue(View.VISIBLE);
    }

    public void showAddPhoto() {
        addPhotoVisibility.setValue(View.VISIBLE);
        imageResultVisibility.setValue(View.GONE);
    }
}