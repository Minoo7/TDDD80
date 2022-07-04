package com.vinga129.savolax.ui.camera;

import android.graphics.Bitmap;
import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {

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
