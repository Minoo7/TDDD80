package com.vinga129.savolax.ui.address;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vinga129.savolax.util.HelperUtil;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentAddressBinding;
import com.vinga129.savolax.retrofit.rest_objects.Address;
import com.vinga129.savolax.retrofit.rest_objects.groups;
import com.vinga129.savolax.retrofit.rest_objects.groups.AddressTypes;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AnnotationContentId(contentId = R.layout.fragment_address)
public class AddressFragment extends FormFragment<Address, FragmentAddressBinding> {

    private AddressViewModel addressViewModel;

    @Override
    protected void initFragment() {
        addressViewModel = new ViewModelProvider(this)
                .get(AddressViewModel.class);

        binding.setAddressTypes(groups.enumToStrings(AddressTypes.values(),
                AddressTypes::name));

        // temp
        binding.fieldAddressType.getEditText().setText("Home");
        binding.fieldStreet.getEditText().setText("Landsvagen 13");
        binding.fieldCity.getEditText().setText("Stockholm");
        binding.fieldZipCode.getEditText().setText("19571");

        formViews.addAll(Arrays
                .asList(binding.fieldAddressType, binding.fieldStreet, binding.fieldCity, binding.fieldZipCode,
                        binding.fieldOtherInfo));

        binding.setViewmodel(addressViewModel);

        addressViewModel.getAddressResult().observe(getViewLifecycleOwner(), addressResult -> {
            if (addressResult == null)
                return;
            binding.loading.setVisibility(View.GONE);
            if (addressResult.getError() != null && addressResult.getError().getErrorMap() != null)
                showErrors(addressResult.getError().getErrorMap());
            if (addressResult.getSuccess() != null)
                updateUi();
        });

        binding.addAddress.setOnClickListener(v -> {
            Address address = null;
            try {
                address = HelperUtil.addFormData(formViews, Address.class);
                binding.loading.setVisibility(View.VISIBLE);
                addressViewModel.addAddress(address);
            } catch (IOException e) {
                makeWarning(requireContext(), binding.container, e.getMessage());
            }
        });
    }

    public void updateUi() {
        CharSequence[] charSequence = new CharSequence[]{"Another address can be added later in settings."};
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Successfully added address")
                .setItems(charSequence, null)
                .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                    Navigation.findNavController(binding.container).popBackStack();
                    //Navigation.findNavController(binding.container).navigate(AddressFragmentDirections.toLoginFragment());
                }).show();
    }
}