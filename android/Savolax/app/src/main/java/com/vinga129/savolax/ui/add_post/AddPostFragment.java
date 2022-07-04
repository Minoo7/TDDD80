package com.vinga129.savolax.ui.add_post;

import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentAddPostBinding;
import com.vinga129.savolax.retrofit.rest_objects.Post;

@AnnotationContentId(contentId = R.layout.fragment_add_post)
public class AddPostFragment extends FormFragment<Post, FragmentAddPostBinding> {

    private AddPostViewModel model;
    public static final int PICK_FILE = 99;

    @Override
    protected void initFragment() {
        model = new ViewModelProvider(requireActivity()).get(AddPostViewModel.class);
        binding.setViewmodel(model);

        binding.setLifecycleOwner(this);

        /*binding.buttonAddPhoto.setOnClickListener((View) -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FILE);
        });*/

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
                uri -> {
                    // Handle the returned Uri
                    //Toast.makeText(context.getApplicationContext(), "ddddd", Toast.LENGTH_LONG).show();
                    Snackbar.make(binding.fieldTitle, "resulted..", Snackbar.LENGTH_LONG).show();
                });

        binding.buttonAddPhoto.setOnClickListener((View) -> {
            mGetContent.launch("image/*");
        });

        binding.buttonTakePhoto.setOnClickListener((view -> {
            ((MainActivity) requireActivity()).requestCameraPermission();
        }));

        binding.buttonCloseImageResult.setOnClickListener((x) -> model.showAddPhoto());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*@Override
    public Post getFormData() {
        //JsonObject formData = new JsonObject();
        //formData.add("title", properFormValue(binding.fieldTitle));
        return null;
    }*/

    /*@Override
    public void updateUi(UserView model) {

    }*/

    @Override
    public void showFail() {

    }
}