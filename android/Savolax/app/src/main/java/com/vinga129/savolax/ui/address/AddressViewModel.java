package com.vinga129.savolax.ui.address;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vinga129.savolax.R;
import com.vinga129.savolax.data.AddressRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups;

public class AddressViewModel extends ViewModel {

    private MutableLiveData<AddressFormState> addressFormState = new MutableLiveData<>();
    private MutableLiveData<AddressResult> addressResult = new MutableLiveData<>();
    private AddressRepository addressRepository;

    private MutableLiveData<String[]> addressTypes = new MutableLiveData<>(groups.enumToStrings(groups.AddressTypes.values(),
            groups.AddressTypes::name));

    AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public LiveData<String[]> getAddressTypes() {
        return addressTypes;
    }

    LiveData<AddressFormState> getAddressFormState() {
        return addressFormState;
    }

    LiveData<AddressResult> getAddressResult() {
        return addressResult;
    }

    public void addAddress(Address address) {
        Result<AddressUser> result = addressRepository.register(address);

        if (result instanceof Result.Success) {
            AddressUser data = ((Result.Success<AddressUser>) result).getData();
            addressResult.setValue(new AddressResult(new AddressUserView(data.getStreet())));
        } else {
            addressResult.setValue(new AddressResult(R.string.login_failed));
        }
    }

    public void addressDataValidate(Address address) {
        // TODO: if error returns error for username for example then set error value returned by request:
        addressFormState.setValue(new AddressFormState("error returned"));
    }

    public void addressDataValidate(Context context) {
        // TODO: if error returns error for username for example then set error value returned by request:
        addressFormState.setValue(new AddressFormState(false));
        Toast.makeText(context.getApplicationContext(), "ddddd", Toast.LENGTH_LONG).show();
    }
}