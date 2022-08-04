package com.vinga129.savolax.ui.login;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.vinga129.savolax.other.User;
import com.vinga129.savolax.other.UserRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@AnnotationContentId(contentId = R.layout.fragment_login)
public class LoginFragment extends FormFragment<Map<String, String>, FragmentLoginBinding> {

    private LoginViewModel loginViewModel;

    @SuppressLint("SetTextI18n")
    @SuppressWarnings({"rawtypes", "ConstantConditions"})
    @Override
    protected void initFragment() {
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        formViews.addAll(Arrays.asList(binding.username, binding.password));

        binding.setFragment(this);

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
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void login() {
        try {
            System.out.println("yuupp");
            binding.loading.setVisibility(View.VISIBLE);
            Class<Map<String, String>> clazz = (Class) Map.class;
            loginViewModel.login(formDataToClass(clazz));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        Navigation.findNavController(binding.container).navigate(LoginFragmentDirections.toRegisterFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}