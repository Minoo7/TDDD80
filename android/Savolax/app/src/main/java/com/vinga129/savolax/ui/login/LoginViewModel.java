package com.vinga129.savolax.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.data.LoginRepository;
import com.vinga129.savolax.data.ResultHolder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<ResultHolder<LoggedInUserView>> loginResult = new MutableLiveData<>();

    public LiveData<ResultHolder<LoggedInUserView>> getLoginResult() {
        return loginResult;
    }

    public void login(Map<String, String> _login) {
        LoginRepository.getInstance().login(_login)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(FormFragment.DefaultSingleFormObserver(loginResult));
    }

    public void loginWithToken(String token) {
        LoginRepository.getInstance().loginWithToken(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(FormFragment.DefaultSingleFormObserver(loginResult));
    }
}