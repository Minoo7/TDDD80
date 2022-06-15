package com.vinga129.savolax.ui.register;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.ResultHolder;
import com.vinga129.savolax.data.RegisterDataSource;
import com.vinga129.savolax.data.RegisterRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.BusinessTypes;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.Genders;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import retrofit2.HttpException;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<ResultHolder<RegisteredUserView>> registerResult = new MutableLiveData<>();
    private final RegisterRepository registerRepository;

    RegisterViewModel() {
        this.registerRepository = RegisterRepository.getInstance();
    }

    LiveData<ResultHolder<RegisteredUserView>> getRegisterResult() {
        return registerResult;
    }

    public void register(Customer customer) {
        registerRepository.register(customer).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                new DisposableSingleObserver<Result<RegisteredUser>>() {
                    @Override
                    public void onSuccess(final Result<RegisteredUser> value) {
                        RegisteredUser data = ((Success<RegisteredUser>) value).getData();
                        registerResult.setValue(new ResultHolder<>(new RegisteredUserView(data.getUsername())));
                    }

                    @Override
                    public void onError(final Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException error = (HttpException) e;
                            try {
                                String errorBody = Objects
                                        .requireNonNull(Objects.requireNonNull(error.response()).errorBody()).string();
                                registerResult.setValue(new ResultHolder<>(new Gson().fromJson(errorBody, Map.class)));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
                });
    }
}