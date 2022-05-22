package com.vinga129.savolax.ui.address;

import static com.vinga129.savolax.HelperUtil.properFormValue;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vinga129.savolax.R;
import com.vinga129.savolax.data.UserView;
import com.vinga129.savolax.data.model.AddressUser;
import com.vinga129.savolax.databinding.FragmentAddressBinding;
import com.vinga129.savolax.ui.FormFragment;
import com.vinga129.savolax.ui.retrofit.rest_objects.Address;

public class AddressFragment extends FormFragment<Address, FragmentAddressBinding> {

    private AddressViewModel addressViewModel;
    // private FragmentAddressBinding binding;

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }*/

    @Override
    protected void initFragmentImpl() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressViewModel = new ViewModelProvider(this, new AddressViewModelFactory())
                .get(AddressViewModel.class);
        binding.setViewmodel(addressViewModel);

        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        addressViewModel.getAddressFormState().observe(getViewLifecycleOwner(), formState -> {
            if (formState == null) {
                return;
            }
            if (formState.getStreetError() != null) {
                // usernameEditText.setError(formState.getUsernameError());
            }
        });

        addressViewModel.getAddressResult().observe(getViewLifecycleOwner(), addressResult -> {
            if (addressResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (addressResult.getError() != null) {
                // showFail(addressResult.getError());
                showFail();
            }
            if (addressResult.getSuccess() != null) {
                updateUi(addressResult.getSuccess());
            }
        });

        registerButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            Address address = getFormData();
            addressViewModel.addAddress(address);
        });
    }

    public Address getFormData() {
        JsonObject formData = new JsonObject();

        formData.add("username", properFormValue(binding.fieldUsername));
        formData.add("first_name", properFormValue(binding.fieldFirstName));
        formData.add("last_name", properFormValue(binding.fieldLastName));
        formData.add("email", properFormValue(binding.fieldEmail));
        formData.add("gender", properFormValue(binding.fieldGender));
        formData.add("phone", properFormValue(binding.fieldPhone));
        formData.add("business_name", properFormValue(binding.fieldBusinessName));
        formData.add("business_type", properFormValue(binding.fieldBusinessType));
        formData.add("organization_number", properFormValue(binding.fieldOrganizationNumber));
        formData.add("bio", properFormValue(binding.fieldBio));
        formData.add("password", properFormValue(binding.fieldBio));

        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(formData), Address.class);
    }

    @Override
    public void updateUi(UserView model) {
        String welcome = getString(R.string.welcome) + ((AddressUserView) model).getUsername();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    /*@Override
    public void updateUi(UserView model) {
        String welcome = getString(R.string.welcome) + model.getUsername();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }*/

    @Override
    public void showFail() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    "errorString",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*private void updateUiWithUser(AddressUserView model) {
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}