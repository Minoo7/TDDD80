package com.vinga129.savolax.data;

import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;

import java.io.IOException;

public class AddressDataSource {
    public Result<AddressUser> addAddress(Address address) {
        try {
            /*Response<Map<String, String>> response = Controller.getUnauthedService().createCustomer(address).execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(null);
            }
            else {
                Map<String, String> errorJson = new Gson().fromJson(response.errorBody().charStream(), Map.class);
                return new Result.Error(errorJson);
            }*/
            // Temp since no network, auto yes:
            return new Result.Success<>(address);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
