package com.vinga129.savolax.data;

import com.vinga129.savolax.ui.address.AddressUserView;
import com.vinga129.savolax.ui.UserRepository;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;

import io.reactivex.Single;

public class AddressDataSource {
    // UserRepository.getINSTANCE().getId()

    public Single<Result<AddressUserView>> addAddress(Address address) {
        return Controller.getInstance().getRestAPI().addAddress(UserRepository.getINSTANCE().getId(), address)
                .flatMap(stringStringMap -> parseToResult(address));
    }

    private Single<Result<AddressUserView>> parseToResult(Address address) {
        return Single.just(new Result.Success<>(
                new AddressUserView(address.getAddress_type(), address.getStreet())));
    }
}
