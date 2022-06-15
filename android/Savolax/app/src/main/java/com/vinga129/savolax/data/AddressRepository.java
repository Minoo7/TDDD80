package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import io.reactivex.Single;

public class AddressRepository {
    private static volatile AddressRepository instance;

    private final AddressDataSource dataSource;

    private AddressRepository(AddressDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static AddressRepository getInstance(AddressDataSource dataSource) {
        if (instance == null)
            instance = new AddressRepository(dataSource);
        return instance;
    }

    public Single<Result<AddressUser>> addAddress(Address address) {
        return dataSource.addAddress(address);
    }
}
