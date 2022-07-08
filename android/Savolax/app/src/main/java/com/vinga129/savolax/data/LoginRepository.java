package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.ui.login.LoggedInUserView;
import io.reactivex.Single;
import java.util.Map;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginRepository() {}

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public Single<Result<LoggedInUserView>> login(Map<String, String> _login) {
        return LoginDataSource.login(_login);
    }


    /**
     * Class that handles authentication w/ login credentials and retrieves user information.
     */
    public static class LoginDataSource {

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
}