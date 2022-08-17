package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.ui.login.LoggedInUserView;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.Map;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public Single<Result> login(Map<String, String> _login) {
        return LoginDataSource.login(_login);
    }

    public Single<Result> loginWithToken(String token) {
        return LoginDataSource.loginWithToken(token);
    }

    public Completable logout() {
        return LoginDataSource.logout();
    }


    /**
     * Class that handles authentication w/ login credentials and retrieves user information.
     */
    public static class LoginDataSource {

        public static Single<Result> login(Map<String, String> _login) {
            return Controller.getInstance().getNoAuthAPI().login(_login)
                    .flatMap(LoginDataSource::parseLoginResult);
        }

        public static Single<Result> loginWithToken(String token) {
            return Controller.getInstance().getNoAuthAPI().loginWithToken("Bearer " + token)
                    .flatMap(LoginDataSource::parseLoginResult);
        }

        public static Single<Result> parseLoginResult(LoggedInUserView loggedInUserView) {
            return Single.just(new Result.Success<>(loggedInUserView));
        }

        public static Completable logout() {
            return Controller.getInstance().getRestAPI().logout();
        }
    }
}