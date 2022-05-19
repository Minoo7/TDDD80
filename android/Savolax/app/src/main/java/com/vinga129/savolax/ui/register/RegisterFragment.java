package com.vinga129.savolax.ui.register;

import static com.vinga129.savolax.HelperUtil.getEditText;
import static com.vinga129.savolax.HelperUtil.properFormValue;
import static com.vinga129.savolax.MainActivity.getContext;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.HelperUtil;
import com.vinga129.savolax.HelperUtil.CustomMapSerializer;
import com.vinga129.savolax.R;
import com.vinga129.savolax.databinding.FragmentRegisterBinding;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.retrofit.rest_objects.MiniCustomer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);
        binding.setViewmodel(registerViewModel);

        final EditText usernameEditText = Objects.requireNonNull(binding.fieldUsername.getEditText());
        final EditText passwordEditText = Objects.requireNonNull(binding.fieldPassword.getEditText());
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                // registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(registerFormState.getUsernameError());
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(registerFormState.getPasswordError());
                }
            }
        });

        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
                System.out.println("hejsan");
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*registerViewModel.registerDataValidate(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());*/
                System.out.println("hejsan");
                registerViewModel.registerDataValidate(getContext());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*registerViewModel.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());*/
                }
                return false;
            }
        });

        registerButton.setOnClickListener(v -> {
            /*loadingProgressBar.setVisibility(View.VISIBLE);

            Customer customer = new Customer(binding.fieldUsername.getEditText().getText(), )

            /*registerViewModel.register(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());*/

            getFormData(view);
        });
    }

    private void getFormData(View view) {
        /*ViewGroup allViews = ((ViewGroup) view);
        Map<String, String> formData = new HashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapter(formData.getClass(), new CustomMapSerializer()).setPrettyPrinting()
                .create();
        for (int i = 0; i < allViews.getChildCount(); i++) {
            View childView = allViews.getChildAt(i);
            if (childView instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) childView;
                formData.put(
                        Objects.requireNonNull(
                                textInputLayout.getHint()).toString(),
                        Objects.requireNonNull(
                                textInputLayout.getEditText()).getText().toString());
            }
        }
        String jsonForm = gson.toJson(formData);*/

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
        Customer customer = gson.fromJson(gson.toJson(formData), Customer.class);
    }

    private void updateUiWithUser(RegisteredUserView model) {
        String welcome = getString(R.string.welcome) + model.getUsername();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}