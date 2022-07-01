package com.vinga129.savolax.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.vinga129.savolax.ui.login.LoggedInUserView;

import com.vinga129.savolax.ui.retrofit.Controller;
import io.reactivex.Single;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static Single<Result<LoggedInUserView>> login(Map<String, String> _login) {
        return Controller.getInstance().getNoAuthAPI().login(_login).flatMap(stringStringMap -> parseResult(_login, stringStringMap));
    }

    private static Single<Result<LoggedInUserView>> parseResult(Map<String, String> _login, Map<String, String> _response) {
        return Single.just(new Result.Success<LoggedInUserView>(
                new LoggedInUserView(_response.get("id"), _login.get("username"), _response.get("first_login"), _response.get("token"))
        ));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}