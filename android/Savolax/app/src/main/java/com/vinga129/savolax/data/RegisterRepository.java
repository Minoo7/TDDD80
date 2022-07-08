package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.ui.register.RegisteredUserView;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import io.reactivex.Single;
import java.util.Map;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterRepository() {
    }

    public static RegisterRepository getInstance() {
        if (instance == null)
            instance = new RegisterRepository();
        return instance;
    }

    public Single<Result<RegisteredUserView>> register(Customer customer) {
        return RegisterDataSource.register(customer);
    }

    public static class RegisterDataSource {

        public static Single<Result<RegisteredUserView>> register(Customer customer) {
            return Controller.getInstance().getNoAuthAPI().createCustomer(customer).flatMap(
                    stringStringMap -> parseResult(customer, stringStringMap));
        }

        private static Single<Result<RegisteredUserView>> parseResult(Customer customer,
                Map<String, String> responseMap) {
            return Single.just(new Result.Success<RegisteredUserView>(
                    new RegisteredUserView(customer.getUsername(), customer.getEmail(),
                            responseMap.get("customer_number"))));
        }
    }

}
