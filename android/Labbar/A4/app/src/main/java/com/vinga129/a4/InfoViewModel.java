package com.vinga129.a4;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {
    private final MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();

    public void setCapturedImage(Bitmap item) {
        capturedImage.setValue(item);
    }

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }


    private final MutableLiveData<Location> location = new MutableLiveData<>();

    public void setLocation(Location item) { location.setValue(item); }

    public LiveData<Location> getLocation() {
        return location;
    }


    private final MutableLiveData<Boolean> cameraPermitted = new MutableLiveData<>();

    public void setCameraPermitted(Boolean bool) {
        cameraPermitted.setValue(bool);
    }

    public LiveData<Boolean> getCameraPermitted() {
        return cameraPermitted;
    }
}
