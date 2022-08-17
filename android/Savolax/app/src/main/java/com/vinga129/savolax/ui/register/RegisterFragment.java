package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentRegisterBinding;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.retrofit.rest_objects.groups;
import com.vinga129.savolax.retrofit.rest_objects.groups.BusinessTypes;
import com.vinga129.savolax.retrofit.rest_objects.groups.Genders;
import java.io.IOException;

@AnnotationContentId(contentId = R.layout.fragment_register)
public class RegisterFragment extends FormFragment<Customer, FragmentRegisterBinding> {

    private RegisterViewModel registerViewModel;

    @Override
    protected void initFragment() {
        registerViewModel = new ViewModelProvider(this)
                .get(RegisterViewModel.class);

        binding.setFragment(this);
        binding.setViewmodel(registerViewModel);
        binding.setGenders(groups.enumToStrings(Genders.values(),
                Genders::name));
        binding.setBusinessTypes(groups.enumToStrings(BusinessTypes.values(),
                BusinessTypes::name));

        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), registerResult -> {
            registerViewModel.showProgress.set(false);
            if (registerResult.getError() != null && registerResult.getError().getErrorMap() != null)
                showErrors(registerResult.getError().getErrorMap());
            else if (registerResult.getSuccess() != null)
                updateUi(registerResult.getSuccess());
        });
    }

    public void register() {
        try {
            Customer customer = formDataToClass(binding.container, Customer.class);
            registerViewModel.showProgress.set(true);
            registerViewModel.register(customer);
        } catch (IOException e) {
            makeWarning(requireContext(), binding.container, e.getMessage());
        }
    }

    public void updateUi(RegisteredUserView model) {
        new MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.register_success))
                .setMessage(getString(R.string.on_new_user_info, model.getCustomer_number(), model.getUsername(),
                        model.getEmail()))
                .setPositiveButton(getString(R.string.ok),
                        (dialogInterface, i) -> navController.navigate(RegisterFragmentDirections.toLoginFragment()))
                .show();
    }
}