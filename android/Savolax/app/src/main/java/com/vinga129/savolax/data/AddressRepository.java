package com.vinga129.savolax.data;

import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.ui.address.AddressUserView;
import com.vinga129.savolax.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.later.UserRepository;
import io.reactivex.Single;

public class AddressRepository {

    private static volatile AddressRepository instance;

    private AddressRepository() {
    }

    public static AddressRepository getInstance() {
        if (instance == null)
            instance = new AddressRepository();
        return instance;
    }

    public Single<Result<AddressUserView>> addAddress(Address address) {
        return AddressDataSource.addAddress(address);
    }

    public static class AddressDataSource {

        public static Single<Result<AddressUserView>> addAddress(Address address) {
            return Controller.getInstance().getRestAPI().addAddress(UserRepository.getINSTANCE().getId(), address)
                    .flatMap(stringStringMap -> parseToResult(address));
        }

        private static Single<Result<AddressUserView>> parseToResult(Address address) {
            return Single.just(new Result.Success<>(
                    new AddressUserView(address.getAddress_type(), address.getStreet())));
        }
    }

}
