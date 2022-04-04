package com.vinga129.a4;

import static android.Manifest.permission.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ImageCapture imageCapture;

    private LocationManager locationManager;

    private TextView locationInfoText;
    private Button captureButton;

    private final String[] CAMERA_PERMISSIONS = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    private final String[] LOCATION_PERMISSIONS = new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};

    private final int CAMERA_REQUEST_CODE = 5;
    private final int LOCATION_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.previewView);
        locationInfoText = findViewById(R.id.locationInfoText);
        captureButton = findViewById(R.id.captureButton);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        captureButton.setOnClickListener((View) -> capturePhoto());

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        if (hasCameraPermission())
            initCamera();
        else
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void initCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                startCameraX(cameraProviderFuture.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // bind to lifecycle:
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return rotateBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrixForRotation = new Matrix();
        matrixForRotation.postRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixForRotation, true);
    }

    private void capturePhoto() {
        imageCapture.takePicture(getExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                previewView.setVisibility(View.GONE);
                captureButton.setVisibility(View.GONE);
                findViewById(R.id.imageShower).setBackground(new BitmapDrawable(getResources(), imageProxyToBitmap(image)));
                if (hasLocationPermissions())
                    updateLocation();
                else
                    requestPermissions(LOCATION_PERMISSIONS, LOCATION_REQUEST_CODE);
            }
        });
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

    private boolean hasCameraPermission() {
        return hasPermission(CAMERA_PERMISSIONS);
    }

    private boolean hasLocationPermissions() {
        return hasPermission(LOCATION_PERMISSIONS);
    }

    @SuppressLint("MissingPermission")
    private void updateLocation() {
        if (hasLocationPermissions()) {
            locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getMainExecutor(), location -> {
                String text = "Location of image:" +
                        "\nLatitude: " + location.getLatitude() +
                        "\nLongitude: " + location.getLongitude();
                locationInfoText.setText(text);
            });
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCamera();
                } else {
                    previewView.setVisibility(View.GONE);
                    locationInfoText.setText("App requires camera permission");
                }
                return;
            }
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                } else {
                    locationInfoText.setText("No location was provided");
                }
            }
        }
    }
}