package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.HelperUtil.makeWarning;
import static com.vinga129.savolax.HelperUtil.properFormValue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.custom.CustomTextInputLayout;
import com.vinga129.savolax.databinding.FragmentLoginBinding;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import retrofit2.HttpException;

@AnnotationContentId(contentId = R.layout.fragment_login)
public class LoginFragment extends FormFragment<Map<String, String>, FragmentLoginBinding> {

    private LoginViewModel loginViewModel;

    @Override
    protected void initFragment() {
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        formViews.addAll(Arrays.asList(binding.username, binding.password));

        // temp
        binding.username.getEditText().setText("rafeb3233");
        binding.password.getEditText().setText("goodPass123");

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult == null)
                return;
            binding.loading.setVisibility(View.GONE);
            if (loginResult.getErrorMap() != null) {
                // formViews.forEach(HelperUtil::clearError);
                Map<String, List<String>> errorMap = loginResult.getErrorMap();
                formViews.stream().filter(field -> errorMap.containsKey(field.getKey())).collect(Collectors.toSet())
                        .forEach(f -> Objects
                                .requireNonNull(f.getEditText())
                                .setError(String.join(",", Objects.requireNonNull(errorMap.get(f.getKey())))));
                // binding.username
            }
            if (loginResult.getError() != null) {
                makeWarning(requireContext(), binding.container, loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {

                SharedPreferences sharedPref = requireContext().getSharedPreferences("API", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("JWT_KEY", loginResult.getSuccess().getJWT_KEY());
                editor.apply();

                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                intent.putExtra("first_login", loginResult.getSuccess().getFirstLogin());
                startActivity(intent);
                requireActivity().finish();
            }
        });

        binding.login.setOnClickListener(v -> {
            try {
                binding.loading.setVisibility(View.VISIBLE);
                Class<Map<String,String>> clazz = (Class<Map<String,String>>)(Class)Map.class;
                loginViewModel.login(addFormData(clazz));
            } catch (IOException e) {
                makeWarning(requireContext(), binding.container, e.getMessage());
            }
        });

        Objects.requireNonNull(binding.password.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.login.setEnabled(true);
            }
        });

        binding.register.setOnClickListener((View) -> Navigation.findNavController(binding.container)
                .navigate(LoginFragmentDirections.toRegisterFragment()));
    }

    @Override
    public void showFail() {

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