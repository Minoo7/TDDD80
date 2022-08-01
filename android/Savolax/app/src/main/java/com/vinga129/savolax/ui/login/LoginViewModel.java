package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.data.LoginRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import retrofit2.HttpException;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<LoggedInUserView>> loginResult = new MutableLiveData<>();

    LiveData<ResultHolder<LoggedInUserView>> getLoginResult() {
        return loginResult;
    }

    @SuppressWarnings("unchecked")
    public void login(Map<String, String> _login) {
        LoginRepository.getInstance().login(_login).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<Result>() {
                            @Override
                            public void onSuccess(final Result value) {
                                LoggedInUserView data = ((Success<LoggedInUserView>) value).getData();
                                loginResult.setValue(new ResultHolder<>(data));
                            }

                            @Override
                            public void onError(final Throwable e) {
                                if (e instanceof HttpException) {
                                    loginResult.setValue(parseHttpError((HttpException) e));
                                }
                            }
                        });
    }
}