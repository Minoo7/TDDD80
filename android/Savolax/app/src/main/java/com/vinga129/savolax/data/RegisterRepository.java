package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.LoggedInUser;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public Result<RegisteredUser> register(Customer customer) {
        // handle login
        Result<RegisteredUser> result = dataSource.register(customer);
        if (result instanceof Result.Success) {
            // setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
            // change back to login fragment
        }
        return result;
    }
}
