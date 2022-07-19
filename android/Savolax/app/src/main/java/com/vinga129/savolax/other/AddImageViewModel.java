package com.vinga129.savolax.other;

import android.graphics.Bitmap;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddImageViewModel extends ViewModel {

    private final MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();
    private final MutableLiveData<Integer> addPhotoVisibility = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> imageResultVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> destinationForResult = new MutableLiveData<>();

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }

    public void setCapturedImage(Bitmap item) {
        capturedImage.setValue(item);
    }

    public LiveData<Integer> getAddPhotoVisibility() {
        return addPhotoVisibility;
    }

    public LiveData<Integer> getImageResultVisibility() {
        return imageResultVisibility;
    }

    public void setDestinationForResult(int destination) {
        destinationForResult.setValue(destination);
    }

    public LiveData<Integer> getDestinationForResult() {
        return destinationForResult;
    }

    public void showImageResult() {
        addPhotoVisibility.setValue(View.GONE);
        imageResultVisibility.setValue(View.VISIBLE);
    }

    public void showAddPhoto() {
        addPhotoVisibility.setValue(View.VISIBLE);
        imageResultVisibility.setValue(View.GONE);
        capturedImage.setValue(null);
    }
}

