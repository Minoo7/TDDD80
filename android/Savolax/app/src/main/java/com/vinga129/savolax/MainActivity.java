package com.vinga129.savolax;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController.OnDestinationChangedListener;
import androidx.navigation.NavDestination;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.vinga129.savolax.databinding.ActivityMainBinding;
import com.vinga129.savolax.retrofit.Controller;

import com.vinga129.savolax.ui.add_post.AddPostFragmentDirections;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private ActivityMainBinding binding;
    private static WeakReference<Context> sContextReference;
    private NavController navController;
    private ActionMode actionMode;

    private View navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sContextReference = new WeakReference<>(getApplicationContext());
        Controller.getInstance().init(getContext());
        // navHostFragment = binding.navHostFragmentActivityMain;
        navHostFragment = findViewById(R.id.nav_host_fragment_activity_main);

        //Bottom Navigation
        BottomNavigationView bottomNav = binding.navView;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Appbar
        Toolbar appBar = binding.topAppBar;
        setSupportActionBar(binding.topAppBar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_products, R.id.navigation_add_post, R.id.navigation_other, R.id.navigation_profile)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Add listener for navigation events to hide Appbar and/or BottomNav for certain fragments
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    boolean showAppBar = true;
                    boolean showBottomNav = true;
                    if (arguments != null) {
                        showAppBar = arguments.getBoolean("ShowAppBar", true);
                        showBottomNav = arguments.getBoolean("ShowBottomNav", true);
                    }
                    if (showAppBar)
                        appBar.setVisibility(View.VISIBLE);
                    else
                        appBar.setVisibility(View.GONE);
                    if (showBottomNav)
                        bottomNav.setVisibility(View.VISIBLE);
                    else
                        bottomNav.setVisibility(View.GONE);
                }
        );

        // check if address already added, if not then go to AddAddressFragment
        if (getIntent().getBooleanExtra("first_login", false)) {
            navController.navigate(R.id.moveToAddressFragment);
        }

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.top_app_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
                return false;
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(navHostFragment, R.string.camera_permission_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Snackbar.make(navHostFragment, R.string.camera_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                // Request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CAMERA);
            }).show();

        } else {
            // Snackbar.make(navHostFragment, R.string.camera_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startCamera() {
        navController.navigate(R.id.moveToCameraFragment);
        // navController.navigate(R.id.moveToCameraFragment);
    }

    public void setCallBack(ActionMode.Callback callback) {
        actionMode = startActionMode(callback);

        findViewById(androidx.appcompat.R.id.action_mode_close_button).setOnClickListener(
                __ -> {
                    navController.popBackStack();
                    actionMode.finish();
                    actionMode = null;
                });
    }

    public void finishActionMode() {
        if (actionMode != null)
            actionMode.finish();
    }
}