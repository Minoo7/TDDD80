package com.vinga129.savolax.ui.add_post;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.view.View;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentAddPostBinding;
import com.vinga129.savolax.databinding.LayoutAddImageBinding;
import com.vinga129.savolax.other.AddImageViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import com.vinga129.savolax.retrofit.rest_objects.groups;
import com.vinga129.savolax.retrofit.rest_objects.groups.PostTypes;
import java.io.IOException;

@AnnotationContentId(contentId = R.layout.fragment_add_post)
public class AddPostFragment extends FormFragment<Post, FragmentAddPostBinding> {

    private AddPostViewModel addPostViewModel;
    private AddImageViewModel addImageViewModel;
    public static final String SELECT_IMAGE = "image/*";
    private boolean imageSelected = false;
    private String currentLocation;

    @Override
    protected void initFragment() {
        LayoutAddImageBinding addImageBinding = binding.layoutAddImage;

        addPostViewModel = new ViewModelProvider(this,
                new AddPostViewModelFactory(requireActivity().getApplication())).get(AddPostViewModel.class);

        addImageViewModel = new ViewModelProvider(requireActivity()).get(AddImageViewModel.class);
        addImageBinding.setViewmodel(addImageViewModel);
        binding.setPostTypes(groups.enumToStrings(PostTypes.values(), PostTypes::name));
        binding.setFragment(this);

        binding.setLifecycleOwner(this);

        PostLocationViewModel postLocationViewModel = new ViewModelProvider(requireActivity())
                .get(PostLocationViewModel.class);
        binding.setLocationViewModel(postLocationViewModel);
        postLocationViewModel.getLocation().observe(getViewLifecycleOwner(), s -> currentLocation = s);

        ((MainActivity) activity).requestLocationPermission();

        setButtonAvailability(binding.buttonPublish, true);

        // imageSelected true if capturedImage is set
        addImageViewModel.getCapturedImage().observe(getViewLifecycleOwner(),
                capturedImage -> imageSelected = capturedImage != null);

        // Set camera button to open camera fragment
        addImageBinding.buttonTakePhoto.setOnClickListener(
                __ -> ((MainActivity) activity).requestCameraPermission());

        // Initialize result launcher for adding photos from camera roll
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
                uri -> {
                    // Handle the returned Uri
                    try {
                        ContentResolver contentResolver = activity.getContentResolver();
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        navController.navigate(R.id.start_add_post);
                        addImageViewModel.setCapturedImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        addImageBinding.buttonAddPhoto.setOnClickListener(
                __ -> mGetContent.launch(SELECT_IMAGE));

        // Reset view
        addImageBinding.buttonCloseImageResult.setOnClickListener(
                __ -> {
                    System.out.println("she on it");
                    addImageViewModel.removeImage();
                });

        addPostViewModel.getAddPostResult().observe(getViewLifecycleOwner(), addPostResult -> {
            if (addPostResult.getError() != null && addPostResult.getError().getErrorMap() != null) {
                showErrors(addPostResult.getError().getErrorMap());
                setButtonAvailability(binding.buttonPublish, true);
            }
            if (addPostResult.isSuccess()) {
                binding.loading.setVisibility(View.GONE);
                requireActivity().getViewModelStore().clear();
                navController.navigate(AddPostFragmentDirections.toHome());
            }
        });
    }

    public void publish() {
        try {
            Post post = formDataToClass(binding.container, Post.class);
            if (currentLocation != null && !currentLocation.isEmpty())
                post.setLocation(currentLocation);
            if (imageSelected)
                addPostViewModel.addPostWithImage(addImageViewModel.getCapturedImage().getValue(), post);
            else
                addPostViewModel.addPost(post);
            setButtonAvailability(binding.buttonPublish, false);
            binding.loading.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            makeWarning(requireContext(), binding.container, e.getMessage());
        }
    }

    private void setButtonAvailability(Button button, boolean value) {
        if (value) {
            button.setClickable(true);
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange));
        } else {
            button.setClickable(false);
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.divider_grey));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addImageViewModel.removeImage();
    }
}