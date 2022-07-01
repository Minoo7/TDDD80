package com.vinga129.savolax.data;

import com.vinga129.savolax.ui.login.LoggedInUserView;
import io.reactivex.Single;
import java.util.Map;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginRepository() {}

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public Single<Result<LoggedInUserView>> login(Map<String, String> _login) {
        return LoginDataSource.login(_login);
    }
}