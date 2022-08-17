package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.register.RegisteredUserView;
import io.reactivex.Single;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterRepository() {
    }

    public static RegisterRepository getInstance() {
        if (instance == null)
            instance = new RegisterRepository();
        return instance;
    }

    public Single<Result> register(Customer customer) {
        return RegisterDataSource.register(customer);
    }

    public static class RegisterDataSource {

        public static Single<Result> register(Customer customer) {
            return Controller.getInstance().getNoAuthAPI().createCustomer(customer).flatMap(
                    stringStringMap -> Single.just(new Result.Success<>(
                            new RegisteredUserView(customer.getUsername(), customer.getEmail(),
                                    stringStringMap.get("customer_number")))));
        }
    }

}
