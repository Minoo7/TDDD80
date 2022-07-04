package com.vinga129.savolax.data;

import com.vinga129.savolax.ui.address.AddressUserView;
import com.vinga129.savolax.retrofit.rest_objects.Address;
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

    public Single<Result<AddressUserView>> addAddress(Address address) {
        return dataSource.addAddress(address);
    }
}
