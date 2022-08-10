package com.vinga129.savolax.ui.profile;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import com.vinga129.savolax.ui.profile.ProfileFragmentDirections.ToListOfCustomers;

@SuppressWarnings("ConstantConditions")
@AnnotationContentId(contentId = R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment<FragmentProfileBinding>
        implements BaseRecyclerAdapter.OnItemListener {

    private ProfileViewModel profileViewModel;

    @Override
    protected void initFragment() {
        // Modify fragment depending on if it's the logged in customer's own profile
        CustomNullableIntegerArgument arg = null;
        try {
            arg = ProfileFragmentArgs.fromBundle(getArguments()).getCustomerId();
        } catch (NullPointerException ignored) {
        }

        int customerId = (arg == null) ? user.getId() : arg.getValue();
        if (customerId == user.getId())
            ((MainActivity) requireActivity()).checkBottomNavItem(4);

        binding.setFragment(this);
        // if customerId is the same as logged in customer id
        // -> change follow button and make profile editing available.
        profileViewModel = new ProfileViewModel(customerId);
        binding.setViewmodel(profileViewModel);

        PostPreviewsRecyclerAdapter adapter = new PostPreviewsRecyclerAdapter(this);
        binding.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int pos, Object data) {
        navController.navigate(ProfileFragmentDirections.toPost((Post) data));
    }

    public void editProfile() {
        navController.navigate(ProfileFragmentDirections.toEditProfile());
    }

    public void showFollowers() {
        if (!profileViewModel.customerProfile.get().getFollowers().isEmpty()) {
            ToListOfCustomers action = ProfileFragmentDirections.toListOfCustomers("Followers",
                    profileViewModel.customerProfile.get().getFollowers().toArray(new MiniCustomer[0]));
            navController.navigate(action);
        }
    }

    public void showFollowing() {
        if (!profileViewModel.customerProfile.get().getFollowing().isEmpty()) {
            ToListOfCustomers action = ProfileFragmentDirections.toListOfCustomers("Following",
                    profileViewModel.customerProfile.get().getFollowing().toArray(new MiniCustomer[0]));
            navController.navigate(action);
        }
    }
}