package com.vinga129.a2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<String> itemDetails = new MutableLiveData<>();

    public void setItem(String item) {
        selectedItem.setValue(item);
    }

    public void setItemDetails(String details) {
        itemDetails.setValue(details);
    }

    public MutableLiveData<String> getItemDetails() {
        return itemDetails;
    }

    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }
}
