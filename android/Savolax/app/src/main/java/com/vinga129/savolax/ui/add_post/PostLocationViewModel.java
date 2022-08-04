package com.vinga129.savolax.ui.add_post;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostLocationViewModel extends ViewModel {
    private final MutableLiveData<String> location = new MutableLiveData<>();
    private final ObservableBoolean showLocationInfo = new ObservableBoolean(setValue());

    public LiveData<String> getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location.setValue(location);
        if (!location.isEmpty()) {
            showLocationInfo.set(true);
        }
    }

    private boolean setValue() {
        return location.getValue() != null && !location.getValue().isEmpty();
    }

    public ObservableBoolean getShowLocationInfo() {
        return showLocationInfo;
    }
}
