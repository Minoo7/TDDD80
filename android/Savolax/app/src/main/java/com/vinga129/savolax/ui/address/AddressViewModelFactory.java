package com.vinga129.savolax.ui.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.data.AddressDataSource;
import com.vinga129.savolax.data.AddressRepository;
import com.vinga129.savolax.data.RegisterDataSource;
import com.vinga129.savolax.data.RegisterRepository;
import com.vinga129.savolax.ui.register.RegisterViewModel;

public class AddressViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddressViewModel.class)) {
            return (T) new AddressViewModel(AddressRepository.getInstance(new AddressDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
