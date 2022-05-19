package com.vinga129.savolax.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.model.LoggedInUser;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;

import java.io.IOException;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDataSource {

    public Result<RegisteredUser> register(Customer customer) {
        try {
            // TODO: handle loggedInUser authentication
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");*/
            Response<Map<String, String>> response = Controller.getUnauthedService().createCustomer(customer).execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(null);
            }
            else {
                Map<String, String> errorJson = new Gson().fromJson(response.errorBody().charStream(), Map.class);
                return new Result.Error(errorJson);
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
