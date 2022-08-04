package com.vinga129.savolax.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vinga129.savolax.base.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class SearchViewModel extends NetworkViewModel {

    private final MutableLiveData<List<MiniCustomer>> customers = new MutableLiveData<>();

    public SearchViewModel() {
        restAPI.getAllCustomersAsMini().doOnSuccess(customers::postValue)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public LiveData<List<MiniCustomer>> getCustomers() {
        return customers;
    }
}
