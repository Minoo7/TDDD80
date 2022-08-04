package com.vinga129.savolax.ui.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.common.util.concurrent.ListenableFuture;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentCameraBinding;
import com.vinga129.savolax.other.AddImageViewModel;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@AnnotationContentId(contentId = R.layout.fragment_camera)
public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;

    private PreviewView previewView;

    private AddImageViewModel addImageViewModel;

    private int lensFacing = CameraSelector.LENS_FACING_BACK;

    @Override
    protected void initFragment() {
        previewView = binding.cameraView;
        ImageButton captureButton = binding.buttonCapture;

        addImageViewModel = new ViewModelProvider(requireActivity()).get(AddImageViewModel.class);

        captureButton.setOnClickListener((View) -> capturePhoto());

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        binding.setFragment(this);

        initCamera();
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(requireContext());
    }

    private void initCamera() {
        cameraProviderFuture.addListener(this::openCamera, getExecutor());
    }

    private void openCamera() {
        try {
            startCameraX(cameraProviderFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing).build();
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
        int degrees = (lensFacing == CameraSelector.LENS_FACING_BACK) ? 90 : 270;
        matrixForRotation.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixForRotation, true);
    }

    private void capturePhoto() {
        imageCapture.takePicture(getExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                addImageViewModel.setCapturedImage(imageProxyToBitmap(image));
                navController.navigate(CameraFragmentDirections.toImageResult());
            }
        });
    }

    public void flipCamera() {
        lensFacing = (lensFacing == CameraSelector.LENS_FACING_BACK) ? CameraSelector.LENS_FACING_FRONT
                : CameraSelector.LENS_FACING_BACK;
        openCamera();
    }
}