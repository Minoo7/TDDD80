package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;

public class AddressRepository {
    private static volatile AddressRepository instance;

    private AddressDataSource dataSource;

    private AddressRepository(AddressDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static AddressRepository getInstance(AddressDataSource dataSource) {
        if (instance == null) {
            instance = new AddressRepository(dataSource);
        }
        return instance;
    }

    public Result<AddressUser> register(Address address) {
        // handle login
        Result<AddressUser> result = dataSource.addAddress(address);
        if (result instanceof Result.Success) {
            // setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
            // change back to login fragment
        }
        return result;
    }
}
