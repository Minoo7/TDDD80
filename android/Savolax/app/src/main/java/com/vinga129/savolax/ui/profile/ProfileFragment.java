package com.vinga129.savolax.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.base.OldBaseFragment;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.post.PostViewModel;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.retrofit.rest_objects.Post;

public class ProfileFragment extends OldBaseFragment implements PostPreviewsRecyclerAdapter.OnItemListener {

    private FragmentProfileBinding binding;
    private PostPreviewsRecyclerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(1);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(profileViewModel);
        mAdapter = new PostPreviewsRecyclerAdapter(this);
        binding.setAdapter(mAdapter);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos, Object data) {
        PostViewModel postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        postViewModel.init((Post) data);
        navController.navigate(MobileNavigationDirections.moveToSpecificFragment());

        //openPost();
    }
}