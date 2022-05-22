package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.MainActivity.getContext;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.R;
import com.vinga129.savolax.data.RegisterRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.BusinessTypes;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.Genders;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    private MutableLiveData<String[]> genders = new MutableLiveData<>(groups.enumToStrings(Genders.values(),
            Genders::name));
    private MutableLiveData<String[]> business_types = new MutableLiveData<>(groups.enumToStrings(BusinessTypes.values(),
            BusinessTypes::name));

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public LiveData<String[]> getGenders() {
        return genders;
    }

    public LiveData<String[]> getBusinessTypes() {
        return business_types;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(Customer customer) {
        Result<RegisteredUser> result = registerRepository.register(customer);

        if (result instanceof Result.Success) {
            RegisteredUser data = ((Success<RegisteredUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisteredUserView(data.getUsername())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void registerDataValidate(Customer customer) {
        // TODO: if error returns error for username for example then set error value returned by request:
        registerFormState.setValue(new RegisterFormState("error returned", null));
    }

    public void registerDataValidate(Context context) {
        // TODO: if error returns error for username for example then set error value returned by request:
        registerFormState.setValue(new RegisterFormState("error returned", null));
        Toast.makeText(context.getApplicationContext(), "ddddd", Toast.LENGTH_LONG).show();
    }
}