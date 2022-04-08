package com.vinga129.a4;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public InfoViewModel infoViewModel;
    // private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;

    public final String[] CAMERA_PERMISSIONS = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    public final String[] LOCATION_PERMISSIONS = new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};

    public final int CAMERA_REQUEST_CODE = 5;
    private final int LOCATION_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        // locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
    }

    private boolean hasPermission(String[] permissions) {
        for (String permission : permissions)
            if (!hasPermission(permission))
                return false;
        return true;
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasCameraPermission() {
        return hasPermission(CAMERA_PERMISSIONS);
    }

    public boolean hasLocationPermissions() {
        return hasPermission(LOCATION_PERMISSIONS);
    }

    @SuppressLint("MissingPermission")
    public void updateLocation() {
        if (hasLocationPermissions()) {
            /*locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getMainExecutor(), location -> {
                Log.d("myLogger", "updateLocation: " + location.getLatitude());
                infoViewModel.setLocation(location);
            });*/
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(this, location -> infoViewModel.setLocation(location));
        }
        else
            requestLocationPermission();
    }

    public Address getLocationAsAddress(Location location) {
        try {
            Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
            return address;
        } catch (IOException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void requestCameraPermission() {
        requestPermissions(CAMERA_PERMISSIONS, CAMERA_REQUEST_CODE);
    }

    public void requestLocationPermission() {
        System.out.println("hejsansvej");
        requestPermissions(LOCATION_PERMISSIONS, LOCATION_REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    infoViewModel.setCameraPermitted(true);
                return;
            }
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    updateLocation();
            }
        }
    }
}