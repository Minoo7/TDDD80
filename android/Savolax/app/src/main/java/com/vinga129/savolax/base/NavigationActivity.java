package com.vinga129.savolax.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

public abstract class NavigationActivity extends AppCompatActivity {
    protected NavController navController;
    public NavController getNavController() {
        return navController;
    }
}
