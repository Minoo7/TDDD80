package com.vinga129.lec2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    public void setItem(String item) {selectedItem.setValue(item);}
    public LiveData<String> getSelectedItem() {return selectedItem;}
}
