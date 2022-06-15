package com.vinga129.savolax.ui.add_post;

import static com.vinga129.savolax.HelperUtil.properFormValue;

import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.vinga129.savolax.data.UserView;
import com.vinga129.savolax.databinding.FragmentAddPostBinding;
import com.vinga129.savolax.ui.FormFragment;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

public class AddPostFragment extends FormFragment<Post, FragmentAddPostBinding> {

    private AddPostViewModel addPostViewModel;
    public static final int PICK_FILE = 99;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPostViewModel = new ViewModelProvider(this).get(AddPostViewModel.class);
        binding.setViewmodel(addPostViewModel);

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
    }

    @Override
    protected void initFragmentImpl() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public Post getFormData() {
        JsonObject formData = new JsonObject();
        formData.add("title", properFormValue(binding.fieldTitle));
        return null;
    }

    @Override
    public void updateUi(UserView model) {

    }

    @Override
    public void showFail() {

    }
}