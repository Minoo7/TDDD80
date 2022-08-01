package com.vinga129.savolax.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vinga129.savolax.base.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import java.util.List;

public class SearchViewModel extends NetworkViewModel {

    private final MutableLiveData<List<MiniCustomer>> customers = new MutableLiveData<>();

    public SearchViewModel() {
        restAPI.getAllCustomersAsMini().doOnSuccess(customers::postValue);
    }

    public LiveData<List<MiniCustomer>> getCustomers() {
        return customers;
    }
}
