package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentLoginBinding;
import com.vinga129.savolax.other.User;
import com.vinga129.savolax.other.UserRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@AnnotationContentId(contentId = R.layout.fragment_login)
public class LoginFragment extends FormFragment<Map<String, String>, FragmentLoginBinding> {

    private LoginViewModel loginViewModel;

    @Override
    protected void initFragment() {
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
        binding.setFragment(this);

        /* Auto login if token is saved: - (uncomment if desired)
        SharedPreferences sharedPref = requireContext().getSharedPreferences("API", Context.MODE_PRIVATE);
        sharedPref.edit().remove("JWT_KEY").apply();
        String token = sharedPref.getString("JWT_KEY", null);
        if (token != null)
            attemptLoginWithToken(token);*/

        formViews.addAll(Arrays.asList(binding.username, binding.password));

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            binding.loading.setVisibility(View.GONE);
            if (loginResult.getError() != null && loginResult.getError().getErrorMap() != null)
                showErrors(loginResult.getError().getErrorMap());
            if (loginResult.getError() != null)
                makeWarning(requireContext(), binding.container, loginResult.getError().getError());
            if (loginResult.getSuccess() != null) {
                // Save JWT Token
                SharedPreferences sharedPref = requireContext().getSharedPreferences("API", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("JWT_KEY", loginResult.getSuccess().getToken());
                editor.apply();

                // Set current user
                UserRepository.getINSTANCE().setUser(new User(loginResult.getSuccess().getId()));

                // Go to MainActivity
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                intent.putExtra("first_login", loginResult.getSuccess().getFirstLogin());
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void login() {
        try {
            binding.loading.setVisibility(View.VISIBLE);
            Class<Map<String, String>> clazz = (Class) Map.class;
            loginViewModel.login(formDataToClass(binding.container, clazz));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        Navigation.findNavController(binding.container).navigate(LoginFragmentDirections.toRegisterFragment());
    }

    private void attemptLoginWithToken(String token) {
        binding.loading.setVisibility(View.VISIBLE);
        loginViewModel.loginWithToken(token);
    }
}