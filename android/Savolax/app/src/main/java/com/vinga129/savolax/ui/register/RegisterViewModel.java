package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.data.RegisterRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import retrofit2.HttpException;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<RegisteredUserView>> registerResult = new MutableLiveData<>();

    LiveData<ResultHolder<RegisteredUserView>> getRegisterResult() {
        return registerResult;
    }

    public void register(Customer customer) {
        RegisterRepository.getInstance().register(customer).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                new DisposableSingleObserver<Result<RegisteredUserView>>() {
                    @Override
                    public void onSuccess(final Result<RegisteredUserView> value) {
                        RegisteredUserView data = ((Success<RegisteredUserView>) value).getData();
                        registerResult.setValue(new ResultHolder<>(data));
                    }

                    @Override
                    public void onError(final Throwable e) {
                        if (e instanceof HttpException) {
                            registerResult.setValue(parseHttpError((HttpException) e));
                        }
                    }
                });
    }
}