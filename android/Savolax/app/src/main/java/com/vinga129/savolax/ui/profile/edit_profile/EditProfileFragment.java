package com.vinga129.savolax.ui.profile.edit_profile;

import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.databinding.FragmentEditProfileBinding;
import com.vinga129.savolax.databinding.LayoutAddImageBinding;
import com.vinga129.savolax.other.AddImageViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import java.io.IOException;

@AnnotationContentId(contentId = R.layout.fragment_edit_profile)
public class EditProfileFragment extends FormFragment<Customer, FragmentEditProfileBinding> {

    private AddImageViewModel addImageViewModel;
    private EditProfileViewModel editProfileViewModel;
    private boolean imageSelected = false;

    @Override
    protected void initFragment() {
        LayoutAddImageBinding addImageBinding = binding.layoutAddImage;

        addImageViewModel = new ViewModelProvider(requireActivity()).get(AddImageViewModel.class);
        editProfileViewModel = new ViewModelProvider(this, new EditProfileViewModelFactory(
                requireActivity().getApplication())).get(EditProfileViewModel.class);

        addImageBinding.setViewmodel(addImageViewModel);
        binding.setViewmodel(editProfileViewModel);
        binding.setFragment(this);

        // imageSelected true if capturedImage is set
        addImageViewModel.getCapturedImage().observe(getViewLifecycleOwner(),
                capturedImage -> imageSelected = capturedImage != null);

        // Set camera button to open camera fragment
        addImageBinding.buttonTakePhoto.setOnClickListener(
                __ -> ((MainActivity) activity).requestCameraPermission());

        formViews.add(binding.fieldBio);

        editProfileViewModel.getEditProfileResult().observe(getViewLifecycleOwner(), editProfileResult -> {
            if (editProfileResult.getError() != null && editProfileResult.getError().getErrorMap() != null)
                showErrors(editProfileResult.getError().getErrorMap());
            else if (editProfileResult.isSuccess())
                navController.navigate(EditProfileFragmentDirections.toProfile());
        });
    }

    public void saveChanges() {
        try {
            Customer customer = formDataToClass(binding.container, Customer.class);
            if (imageSelected) {
                editProfileViewModel
                        .editProfileWithImage(addImageViewModel.getCapturedImage().getValue(), customer);
            } else
                editProfileViewModel.editProfile(customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addImageViewModel.removeImage();
    }
}