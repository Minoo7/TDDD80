package com.vinga129.savolax.ui.address;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.data.AddressRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.retrofit.rest_objects.Address;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AddressViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<AddressUserView>> addressResult = new MutableLiveData<>();

    LiveData<ResultHolder<AddressUserView>> getAddressResult() {
        return addressResult;
    }

    @SuppressWarnings("unchecked")
    public void addAddress(Address address) {
        AddressRepository.getInstance().addAddress(address).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<Result>() {
                            @Override
                            public void onSuccess(final Result value) {
                                AddressUserView data = ((Success<AddressUserView>) value).getData();
                                addressResult.setValue(new ResultHolder<>(data));
                            }

                            @Override
                            public void onError(final Throwable e) {
                                if (e instanceof HttpException) {
                                    addressResult.setValue(parseHttpError((HttpException) e));
                                }
                            }
                        });
    }
}