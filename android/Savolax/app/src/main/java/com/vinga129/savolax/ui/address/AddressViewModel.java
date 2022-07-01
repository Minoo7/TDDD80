package com.vinga129.savolax.ui.address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.base.ResultHolder;
import com.vinga129.savolax.data.AddressRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.Objects;
import retrofit2.HttpException;

public class AddressViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<AddressUserView>> addressResult = new MutableLiveData<>();
    private final AddressRepository addressRepository;

    AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    LiveData<ResultHolder<AddressUserView>> getAddressResult() {
        return addressResult;
    }

    public void addAddress(Address address) {
        addressRepository.addAddress(address).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<Result<AddressUserView>>() {
                            @Override
                            public void onSuccess(final Result<AddressUserView> value) {
                                AddressUserView data = ((Success<AddressUserView>) value).getData();
                                addressResult.setValue(new ResultHolder<>(data));
                            }

                            @Override
                            public void onError(final Throwable e) {
                                if (e instanceof HttpException) {

                                    try {
                                        String errorBody = Objects
                                                .requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string();
                                        System.out.println("ddddd");
                                        System.out.println(errorBody);
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                    /*NetworkUtil.handleOnError((HttpException) e,
                                            (errorBody) -> addressResult.setValue(new ResultHolder<>(
                                                    new Gson().fromJson(errorBody, Map.class))));*/
                                }
                            }
                        });
    }
}