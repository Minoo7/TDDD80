package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import io.reactivex.Single;

public class RegisterDataSource {

    public static Single<Result<RegisteredUser>> register(Customer customer) {
        return Controller.getInstance().getNoAuthAPI().createCustomer(customer).flatMap(
                stringStringMap -> parseL(customer));
    }

    private static Single<Result<RegisteredUser>> parseL(Customer customer) {
        return Single.just(new Result.Success<RegisteredUser>(
                new RegisteredUser(customer.getUsername(), customer.getEmail(), customer.getCustomer_number())));
    }
}
