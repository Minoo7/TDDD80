package com.vinga129.savolax.other;

import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddImageViewModel extends ViewModel {

    private final MutableLiveData<Bitmap> capturedImage = new MutableLiveData<>();

    public LiveData<Bitmap> getCapturedImage() {
        return capturedImage;
    }

    public void setCapturedImage(Bitmap item) {
        capturedImage.setValue(item);
    }

    public void removeImage() {
        capturedImage.setValue(null);
    }
}

