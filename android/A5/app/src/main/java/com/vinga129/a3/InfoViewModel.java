package com.vinga129.a3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {
    private final MutableLiveData<GroupsContent.GroupItem> selectedItem = new MutableLiveData<>();

    public void setItem(GroupsContent.GroupItem item) {
        selectedItem.setValue(item);
    }

    public LiveData<GroupsContent.GroupItem> getSelectedItem() {
        return selectedItem;
    }
}
