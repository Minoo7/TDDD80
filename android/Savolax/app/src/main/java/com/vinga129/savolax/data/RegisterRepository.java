package com.vinga129.savolax.data;

import com.vinga129.savolax.ui.register.RegisteredUserView;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import io.reactivex.Single;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterRepository() {}

    public static RegisterRepository getInstance() {
        if (instance == null)
            instance = new RegisterRepository();
        return instance;
    }

    public Single<Result<RegisteredUserView>> register(Customer customer) {
        return RegisterDataSource.register(customer);
    }
}
