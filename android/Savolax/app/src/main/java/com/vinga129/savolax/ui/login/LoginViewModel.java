package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.HelperUtil.parseHttpError;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.base.ResultHolder;
import com.vinga129.savolax.data.LoginRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.Map;
import retrofit2.HttpException;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<LoggedInUserView>> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<ResultHolder<LoggedInUserView>> getLoginResult() {
        return loginResult;
    }

    public void login(Map<String, String> _login) {
        loginRepository.login(_login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<Result<LoggedInUserView>>() {
                            @Override
                            public void onSuccess(final Result<LoggedInUserView> value) {
                                LoggedInUserView data = ((Success<LoggedInUserView>) value).getData();
                                loginResult.setValue(new ResultHolder<>(data));
                            }

                            @Override
                            public void onError(final Throwable e) {
                                if (e instanceof HttpException) {
                                    try {
                                        loginResult.setValue(parseHttpError((HttpException) e));
                                    } catch (IOException ioException) {
                                        ioException.printStackTrace();
                                    }
                                }
                            }
                        });
    }
}