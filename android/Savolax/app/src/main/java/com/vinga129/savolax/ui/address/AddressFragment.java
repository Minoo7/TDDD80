package com.vinga129.savolax.ui.address;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.view.View;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentAddressBinding;
import com.vinga129.savolax.retrofit.rest_objects.Address;
import com.vinga129.savolax.retrofit.rest_objects.groups;
import com.vinga129.savolax.retrofit.rest_objects.groups.AddressTypes;
import java.io.IOException;

@AnnotationContentId(contentId = R.layout.fragment_address)
public class AddressFragment extends FormFragment<Address, FragmentAddressBinding> {

    private AddressViewModel addressViewModel;

    @Override
    protected void initFragment() {
        addressViewModel = new ViewModelProvider(this)
                .get(AddressViewModel.class);

        binding.setViewmodel(addressViewModel);
        binding.setFragment(this);
        binding.setAddressTypes(groups.enumToStrings(AddressTypes.values(),
                AddressTypes::name));

        addressViewModel.getAddressResult().observe(getViewLifecycleOwner(), addressResult -> {
            binding.loading.setVisibility(View.GONE);
            if (addressResult.getError() != null && addressResult.getError().getErrorMap() != null)
                showErrors(addressResult.getError().getErrorMap());
            if (addressResult.getSuccess() != null)
                updateUi();
        });
    }

    public void addAddress() {
        try {
            Address address = formDataToClass(binding.container, Address.class);
            binding.loading.setVisibility(View.VISIBLE);
            addressViewModel.addAddress(address);
        } catch (IOException e) {
            makeWarning(requireContext(), binding.container, e.getMessage());
        }
    }

    public void updateUi() {
        new MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.address_success))
                .setMessage(getString(R.string.on_added_address))
                .setPositiveButton(getResources().getString(R.string.ok),
                        (dialogInterface, i) -> Navigation.findNavController(binding.container).popBackStack())
                .show();
    }
}