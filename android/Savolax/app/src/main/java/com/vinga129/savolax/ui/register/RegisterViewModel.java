package com.vinga129.savolax.ui.register;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.data.RegisterRepository;
import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<RegisteredUserView>> registerResult = new MutableLiveData<>();
    public final ObservableBoolean showProgress = new ObservableBoolean(false);

    public LiveData<ResultHolder<RegisteredUserView>> getRegisterResult() {
        return registerResult;
    }

    public void register(Customer customer) {
        RegisterRepository.getInstance().register(customer)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(FormFragment.DefaultSingleFormObserver(registerResult));
    }
}