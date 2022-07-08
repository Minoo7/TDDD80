package com.vinga129.savolax.ui.add_post;

import static com.vinga129.savolax.util.HelperUtil.makeWarning;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AnnotationContentId(contentId = R.layout.fragment_add_post)
public class AddPostFragment extends FormFragment<Post, FragmentAddPostBinding> {

    private LayoutAddImageBinding addImageBinding;
    private AddPostViewModel addPostViewModel;
    private AddImageViewModel addImageViewModel;
    public static final int PICK_FILE = 99;
    private static final String SELECT_IMAGE = "image/*";
    private boolean imageSelected = false;

    @Override
    protected void initFragment() {
        addImageBinding = binding.layoutAddImage;

        addPostViewModel = new ViewModelProvider(requireActivity()).get(AddPostViewModel.class);
        addImageViewModel = new ViewModelProvider(requireActivity()).get(AddImageViewModel.class);
        addImageBinding.setViewmodel(addImageViewModel);
        binding.setPostTypes(groups.enumToStrings(PostTypes.values(), PostTypes::name));
        // binding.setLifecycleOwner(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

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
                        addImageViewModel.setCapturedImage(bitmap);
                        addImageViewModel.showImageResult();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        addImageBinding.buttonAddPhoto.setOnClickListener(
                __ -> mGetContent.launch(SELECT_IMAGE));

        // Reset view
        addImageBinding.buttonCloseImageResult.setOnClickListener(
                __ -> addImageViewModel.showAddPhoto());

        // Initialize form
        formViews.addAll(Arrays.asList(binding.fieldTitle, binding.fieldPostType, binding.fieldPostType));

        addPostViewModel.getAddPostResult().observe(getViewLifecycleOwner(), booleanResultHolder -> {
            if (booleanResultHolder == null)
                return;
            if (booleanResultHolder.getError() != null && booleanResultHolder.getError().getErrorMap() != null) {
                Map<String, List<String>> errorMap = booleanResultHolder.getError().getErrorMap();
                formViews.stream().filter(field -> errorMap.containsKey(field.getKey())).collect(Collectors.toSet())
                        .forEach(f -> Objects.requireNonNull(f.getEditText())
                                .setError(String.join(",", Objects.requireNonNull(errorMap.get(f.getKey())))));
            }
            if (booleanResultHolder.getSuccess() != null) {
                makeWarning(requireContext(), binding.container, "success");
            }
        });

        binding.buttonPublish.setOnClickListener(__ -> {
            try {
                Post post = addFormData(Post.class);
                if (imageSelected)
                    addPostViewModel.addPostWithImage(addImageViewModel.getCapturedImage().getValue(), post);
                else
                    addPostViewModel.addPost(post);
            } catch (IOException e) {
                makeWarning(requireContext(), binding.container, e.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addImageViewModel.showAddPhoto();
    }
}