package com.vinga129.a4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraFragment extends BaseFragment {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;

    private PreviewView previewView;
    private Button captureButton;

    @Override
    public int getLayout() {
        return R.layout.fragment_camera;
    }

    @Override
    public void init() {
        previewView = view.findViewById(R.id.previewView);
        captureButton = view.findViewById(R.id.captureButton);

        captureButton.setOnClickListener((View) -> capturePhoto());

        cameraProviderFuture = ProcessCameraProvider.getInstance(main);

        model.getCameraPermitted().observe(getViewLifecycleOwner(), bool -> {
            if (bool)
                initCamera();
        });

        if (main.hasCameraPermission())
            initCamera();
        else
            main.requestCameraPermission();
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(main);
    }

    public void initCamera() {
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
                model.setCapturedImage(imageProxyToBitmap(image));
                navController.navigate(CameraFragmentDirections.toImageViewerFragment());
                main.updateLocation();
            }
        });
    }
}