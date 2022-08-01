package com.vinga129.savolax.ui.profile;

import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.retrofit.rest_objects.Post;

@AnnotationContentId(contentId = R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment<FragmentProfileBinding>
        implements BaseRecyclerAdapter.OnItemListener {

    @Override
    protected void initFragment() {
        // Modify fragment depending on if it's the logged in customer's own profile
        CustomNullableIntegerArgument arg = null;
        try {
            arg = ProfileFragmentArgs.fromBundle(getArguments()).getCustomerId();
        }
        catch (NullPointerException e) { }

        int customerId = (arg == null) ? user.getId() : arg.getValue();

        ProfileViewModel profileViewModel = new ProfileViewModel(customerId);
        // if customerId is the same as logged in customer id
        // -> change follow button and make profile editing available.

        binding.setViewmodel(profileViewModel);
        PostPreviewsRecyclerAdapter adapter = new PostPreviewsRecyclerAdapter(this);
        binding.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int pos, Object data) {
        navController.navigate(MobileNavigationDirections.moveToSpecificPost((Post) data));
    }

    public void editProfile() {
        navController.navigate(ProfileFragmentDirections.toEditProfile());
    }
}