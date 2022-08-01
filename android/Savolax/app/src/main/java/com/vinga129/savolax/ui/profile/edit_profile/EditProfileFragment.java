package com.vinga129.savolax.ui.profile.edit_profile;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.FormFragment;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentEditProfileBinding;
import com.vinga129.savolax.databinding.LayoutAddImageBinding;
import com.vinga129.savolax.other.AddImageViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.ui.profile.edit_profile.EditProfileFragmentDirections.ToProfileAfterEdit;
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

        // imageSelected true if capturedImage is set
        addImageViewModel.getCapturedImage().observe(getViewLifecycleOwner(),
                capturedImage -> imageSelected = capturedImage != null);

        // Set camera button to open camera fragment
        addImageBinding.buttonTakePhoto.setOnClickListener(
                __ ->  {
                    addImageViewModel.setDestinationForResult(R.id.image_to_edit_profile);
                    ((MainActivity) activity).requestCameraPermission();
                });

        formViews.add(binding.fieldBio);

        ActionMode.Callback callback = new Callback() {
            @Override
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                requireActivity().getMenuInflater().inflate(R.menu.image_result, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.accept) {
                    try {
                        Customer customer = formDataToClass(Customer.class);
                        if (imageSelected) {
                            editProfileViewModel
                                    .editProfileWithImage(addImageViewModel.getCapturedImage().getValue(), customer);
                        } else
                            editProfileViewModel.editProfile(customer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // actionMode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        };
        ((MainActivity) requireActivity()).setCallBack(callback);

        editProfileViewModel.getEditProfileResult().observe(getViewLifecycleOwner(), editProfileResult -> {
            if (editProfileResult.getError() != null && editProfileResult.getError().getErrorMap() != null)
                showErrors(editProfileResult.getError().getErrorMap());
            else if (editProfileResult.isSuccess()) {
                ToProfileAfterEdit action = EditProfileFragmentDirections.toProfileAfterEdit().setCustomerId(new CustomNullableIntegerArgument(
                        user.getId()));
                navController.navigate(action);
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();

        ((MainActivity) requireActivity()).finishActionMode();
        addImageViewModel.showAddPhoto();
    }
}