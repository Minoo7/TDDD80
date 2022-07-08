package com.vinga129.savolax.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.post.PostViewModel;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.retrofit.rest_objects.Post;

@AnnotationContentId(contentId = R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment<FragmentProfileBinding>
        implements PostPreviewsRecyclerAdapter.OnItemListener {

    private PostPreviewsRecyclerAdapter adapter;
    private ProfileViewModel profileViewModel;
    // private boolean personal = false;

    @Override
    protected void initFragment() {
        // Modify fragment depending on if it's the logged in customer's own profile
        CustomNullableIntegerArgument arg = ProfileFragmentArgs.fromBundle(getArguments()).getCustomerId();
        Integer customerId = (arg != null) ? arg.getValue() : null;
        // if no value was provided or it's the same as logged in customer id
        // -> change follow button and make profile editing available.
        if (arg == null || customerId == null || customerId == user.getId()) {
            binding.buttonFollowOrEdit.setText("Edit");
            binding.buttonFollowOrEdit.setOnClickListener(__ -> editProfile());
        }

        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(1))
                .get(ProfileViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(profileViewModel);
        adapter = new PostPreviewsRecyclerAdapter(this);
        binding.setAdapter(adapter);
    }



    @Override
    public void onItemClick(int pos, Object data) {
        PostViewModel postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        postViewModel.init((Post) data);
        navController.navigate(MobileNavigationDirections.moveToSpecificFragment());
    }

    private void editProfile() {
        navController.navigate(ProfileFragmentDirections.toEditProfile());
    }
}