package com.vinga129.savolax;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.AttributeSet;
import android.view.MenuItem;

import android.view.View;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vinga129.savolax.databinding.ActivityMainBinding;
import com.vinga129.savolax.ui.retrofit.Controller;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static WeakReference<Context> sContextReference;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sContextReference = new WeakReference<>(getApplicationContext());

        Controller.getInstance().init(getContext());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_products, R.id.navigation_add_post, R.id.navigation_other, R.id.navigation_account)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // check if address already added, if not then go to AddAddressFragment
        if (getIntent().getBooleanExtra("first_login", false)) {
            navController.navigate(R.id.moveToAddressFragment);
        }
    }

    public static Context getContext() {
        return sContextReference.get();
    }

    public NavController getNavController() {
        return navController;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navController.popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.navigateUp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}