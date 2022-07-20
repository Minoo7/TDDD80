package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentLoginBinding;
import com.vinga129.savolax.ui.later.User;
import com.vinga129.savolax.ui.later.UserRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@SuppressLint("NonConstantResourceId")
@AnnotationContentId(contentId = R.layout.fragment_login)
public class LoginFragment extends FormFragment<Map<String, String>, FragmentLoginBinding> {

    private LoginViewModel loginViewModel;

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void initFragment() {
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        formViews.addAll(Arrays.asList(binding.username, binding.password));

        // temp
        binding.username.getEditText().setText("rafeb3233");
        binding.password.getEditText().setText("goodPass123");

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult == null)
                return;
            binding.loading.setVisibility(View.GONE);
            if (loginResult.getError() != null && loginResult.getError().getErrorMap() != null)
                showErrors(loginResult.getError().getErrorMap());
            if (loginResult.getError() != null)
                makeWarning(requireContext(), binding.container, loginResult.getError().getError());
            if (loginResult.getSuccess() != null) {
                // Save JWT Token
                SharedPreferences sharedPref = requireContext().getSharedPreferences("API", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("JWT_KEY", loginResult.getSuccess().getJWT_KEY());
                editor.apply();

                // Set current user
                UserRepository.getINSTANCE().setUser(new User(loginResult.getSuccess().getUserId()));

                // Go to MainActivity
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
                Class<Map<String, String>> clazz = (Class<Map<String, String>>) (Class) Map.class;
                loginViewModel.login(formDataToClass(clazz));
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