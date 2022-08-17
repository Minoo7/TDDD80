package com.vinga129.savolax;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;
import static com.vinga129.savolax.util.BindingUtils.booleanToVisibility;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.vinga129.savolax.base.NavigationActivity;
import com.vinga129.savolax.databinding.ActivityMainBinding;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.ui.add_post.PostLocationViewModel;
import com.vinga129.savolax.ui.home.HomeFragmentDirections;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends NavigationActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private ActivityMainBinding binding;
    private static WeakReference<Context> sContextReference;
    private ActionMode actionMode;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private PostLocationViewModel postLocationViewModel;

    private View navHostFragment;
    private BottomNavigationView bottomNav;
    private final List<Integer> bottomNavItems = new ArrayList<>();

    private final static Map<Integer, Integer> navItemDestinationMap = Collections.unmodifiableMap(
            new HashMap<Integer, Integer>() {
                {
                    put(R.id.navigation_home, R.id.start_home);
                    put(R.id.navigation_products, R.id.start_products);
                    put(R.id.navigation_add_post, R.id.start_add_post);
                    put(R.id.navigation_more, R.id.start_more);
                    put(R.id.nested_profile, R.id.start_profile);
                }
            });

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContextReference = new WeakReference<>(getApplicationContext());
        Controller.getInstance().init(getContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navHostFragment = findViewById(R.id.nav_host_fragment_activity_main);

        //Bottom Navigation
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        bottomNav = binding.navView;
        for (int i = 0; i < bottomNav.getMenu().size(); i++)
            bottomNavItems.add(bottomNav.getMenu().getItem(i).getItemId());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        postLocationViewModel = new ViewModelProvider(this).get(PostLocationViewModel.class);

        // Appbar
        Toolbar appBar = binding.topAppBar;
        setSupportActionBar(binding.topAppBar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_products, R.id.navigation_add_post, R.id.navigation_more, R.id.nested_profile)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Add listener for navigation events to hide Appbar and/or BottomNav for certain fragments
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    int showAppBar = View.VISIBLE;
                    int showBottomNav = View.VISIBLE;
                    if (arguments != null) {
                        showAppBar = booleanToVisibility(arguments.getBoolean("ShowAppBar", true));
                        showBottomNav = booleanToVisibility(arguments.getBoolean("ShowBottomNav", true));
                    }
                    appBar.setVisibility(showAppBar);
                    bottomNav.setVisibility(showBottomNav);

                    // update bottomNav to match
                    if (bottomNavItems.contains(destination.getId()))
                        bottomNav.getMenu().getItem(bottomNavItems.indexOf(destination.getId())).setChecked(true);
                    else
                        uncheckAllItems();
                }
        );

        bottomNav.setOnItemSelectedListener(item -> {
            navController.navigate(navItemDestinationMap.get(item.getItemId()));
            return true;
        });

        // check if address already added, if not then go to AddAddressFragment
        if (getIntent().getBooleanExtra("first_login", false)) {
            navController.navigate(HomeFragmentDirections.toAddAddress());
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

    public void checkBottomNavItem(int index) {
        bottomNav.getMenu().getItem(index).setChecked(true);
    }

    private void uncheckAllItems() {
        Menu menu = bottomNav.getMenu();
        menu.setGroupCheckable(0, true, false);
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setChecked(false);
        menu.setGroupCheckable(0, true, true);
    }

    public static Context getContext() {
        return sContextReference.get();
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
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
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
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @SuppressLint("MissingPermission")
    private void sendLocation() {
        fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(
                location -> {
                    try {
                        android.location.Address address = geocoder
                                .getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                        postLocationViewModel.setLocation(address.getAddressLine(0).split(",")[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result
                        .getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result
                        .getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null
                        && fineLocationGranted) {
                    // Precise location access granted.
                    sendLocation();
                } else if (coarseLocationGranted != null
                        && coarseLocationGranted) {
                    // Only approximate location access granted.
                    sendLocation();
                } else {
                    // No location access granted.
                    postLocationViewModel.setLocation("");
                }
            }
    );

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            sendLocation();
        } else {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void startCamera() {
        navController.navigate(MobileNavigationDirections.toCamera());
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

    public void setCallBackWithTitle(ActionMode.Callback callback, String title) {
        setCallBack(callback);
        actionMode.setTitle(title);
    }

    public void finishActionMode() {
        if (actionMode != null)
            actionMode.finish();
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}