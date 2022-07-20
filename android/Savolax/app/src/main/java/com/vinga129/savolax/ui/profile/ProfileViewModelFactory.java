package com.vinga129.savolax.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final int customerId;

    public ProfileViewModelFactory(int customerId) {
        this.customerId = customerId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(customerId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}