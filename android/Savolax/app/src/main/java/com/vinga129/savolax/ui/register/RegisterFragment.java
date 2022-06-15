package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.HelperUtil.makeWarning;
import static com.vinga129.savolax.HelperUtil.properFormValue;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.databinding.FragmentRegisterBinding;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.BusinessTypes;
import com.vinga129.savolax.ui.retrofit.rest_objects.groups.Genders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AnnotationContentId(contentId = R.layout.fragment_register)
public class RegisterFragment extends FormFragment<Customer, FragmentRegisterBinding> {

    private RegisterViewModel registerViewModel;
    List<CustomTextInputLayout> formViews = new ArrayList<>();

    @Override
    protected void initFragment() {
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
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
            if (registerResult.getErrorMap() != null) {
                Map<String, List<String>> errorMap = registerResult.getErrorMap();
                formViews.stream().filter(field -> errorMap.containsKey(field.getKey())).collect(Collectors.toSet())
                        .forEach(f -> Objects.requireNonNull(f.getEditText())
                                .setError(String.join(",", Objects.requireNonNull(errorMap.get(f.getKey())))));
                binding.fieldUsername.setError("");
            }
            if (registerResult.getSuccess() != null) {
                updateUi(registerResult.getSuccess());
            }
        });

        binding.register.setOnClickListener(v -> {
            try {
                // formViews.forEach(this::clearError);
                Customer customer = addFormData();
                binding.loading.setVisibility(View.VISIBLE);
                registerViewModel.register(customer);
            } catch (IOException e) {
                makeWarning(requireContext(), binding.container, e.getMessage());
            }
        });
    }

    private void clearError(CustomTextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public Customer addFormData() throws IOException {
        JsonObject formData = new JsonObject();

        for (CustomTextInputLayout formView : formViews) {
            JsonElement value = properFormValue(formView);
            if (value.isJsonNull() && formView.isRequired())
                throw new IOException("Required fields can not be empty");
            formData.add(formView.getKey(), value);
        }

        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(formData), Customer.class);
    }

    // @Override
    public void updateUi(RegisteredUserView model) {
        /*String welcome = getString(R.string.welcome) + ((RegisteredUserView) model).getUsername();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }*/
        CharSequence[] charSequence = new CharSequence[]{"hejsan"};
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Successfully registered")
                .setItems(charSequence, null)
                .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                    
                }).show();

        /*val items = arrayOf("Item 1", "Item 2", "Item 3")

        MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.title))
                .setItems(items) { dialog, which ->
            // Respond to item chosen
        }
        .show()*/
    }

    @Override
    public void showFail() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    "error text string test",
                    Toast.LENGTH_LONG).show();
        }
    }
}