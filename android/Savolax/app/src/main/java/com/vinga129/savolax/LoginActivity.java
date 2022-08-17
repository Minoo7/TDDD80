package com.vinga129.savolax;

import android.os.Bundle;
import androidx.navigation.Navigation;
import com.vinga129.savolax.base.NavigationActivity;

public class LoginActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navController = Navigation.findNavController(this, R.id.navcontainerviewlogin);
    }
}