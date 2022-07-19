package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.view.View;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import java.util.Arrays;

@AnnotationContentId(contentId = R.layout.fragment_register)
public class RegisterFragment extends FormFragment<Customer, FragmentRegisterBinding> {

    private RegisterViewModel registerViewModel;

    @Override
    protected void initFragment() {
        registerViewModel = new ViewModelProvider(this)
                .get(RegisterViewModel.class);

        binding.setGenders(groups.enumToStrings(Genders.values(),
                Genders::name));
        binding.setBusinessTypes(groups.enumToStrings(BusinessTypes.values(),
                BusinessTypes::name));

        // temp
        binding.fieldUsername.getEditText().setText("rafeb3233");
        binding.fieldFirstName.getEditText().setText("Rafeb");
        binding.fieldLastName.getEditText().setText("Laban");
        binding.fieldEmail.getEditText().setText("rafeb3233@gmail.com");
        binding.fieldGender.getEditText().setText("Man");
        binding.fieldPhone.getEditText().setText("0769541211");
        binding.fieldBusinessName.getEditText().setText("Espresso House");
        binding.fieldBusinessType.getEditText().setText("Cafe");
        binding.fieldOrganizationNumber.getEditText().setText("12345678911");
        binding.fieldBio.getEditText().setText("hello this is our bio");
        binding.fieldPassword.getEditText().setText("goodPass123");

        formViews.addAll(Arrays
                .asList(binding.fieldUsername, binding.fieldFirstName, binding.fieldLastName, binding.fieldEmail,
                        binding.fieldGender, binding.fieldPhone, binding.fieldBusinessName, binding.fieldBusinessType,
                        binding.fieldOrganizationNumber, binding.fieldBio, binding.fieldPassword));

        binding.setViewmodel(registerViewModel);

        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), registerResult -> {
            if (registerResult == null)
                return;
            binding.loading.setVisibility(View.GONE);
            if (registerResult.getError() != null && registerResult.getError().getErrorMap() != null)
                showErrors(registerResult.getError().getErrorMap());
            if (registerResult.getSuccess() != null)
                updateUi(registerResult.getSuccess());
        });

        binding.register.setOnClickListener(v -> {
            try {
                Customer customer = formDataToClass(Customer.class);
                binding.loading.setVisibility(View.VISIBLE);
                registerViewModel.register(customer);
            } catch (IOException e) {
                makeWarning(requireContext(), binding.container, e.getMessage());
            }
        });
    }


    public void updateUi(RegisteredUserView model) {
        CharSequence[] charSequence = new CharSequence[]{getString(R.string.on_new_user_info),
                "Customer number: " + model.getCustomer_number(), "---Other login methods:---",
                "username: " + model.getUsername() + "\nemail: " + model.getEmail()};
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Successfully registered")
                .setItems(charSequence, null)
                .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                    Navigation.findNavController(binding.container)
                            .navigate(RegisterFragmentDirections.toLoginFragment());
                }).show();
    }
}