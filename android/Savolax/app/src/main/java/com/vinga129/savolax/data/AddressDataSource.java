package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.ui.UserRepository;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;

import io.reactivex.Single;

public class AddressDataSource {
    public Single<Result<AddressUser>> addAddress(Address address) {
        return Controller.getInstance().getNoAuthAPI().addAddress(UserRepository.getINSTANCE().getId(), address).flatMap(
                stringStringMap -> parseToResult(address));
    }

    private Single<Result<AddressUser>> parseToResult(Address address) {
        return Single.just(new Result.Success<>(
                new AddressUser(address.getAddress_type(), address.getStreet())));
    }
}
