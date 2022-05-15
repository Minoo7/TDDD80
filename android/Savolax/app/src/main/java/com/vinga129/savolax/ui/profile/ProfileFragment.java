package com.vinga129.savolax.ui.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.NetworkReceiver;
import com.vinga129.savolax.R;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.BaseFragment;
import com.vinga129.savolax.ui.post.PostViewModel;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends BaseFragment implements PostPreviewsRecyclerAdapter.OnItemListener {

    private FragmentProfileBinding binding;
    private PostPreviewsRecyclerAdapter mAdapter;
    public NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(1);

        // POSTS SKA NOG VARA EN ACTIVITY KANSKE JOO
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(profileViewModel);
        mAdapter = new PostPreviewsRecyclerAdapter(this);
        binding.setAdapter(mAdapter);

        View root = binding.getRoot();

        navController = main.getNavController();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos, Object data) {
        PostViewModel postViewModel = new ViewModelProvider(main).get(PostViewModel.class);
        postViewModel.init((Post) data);
        navController.navigate(MobileNavigationDirections.moveToSpecificFragment());
    }
}