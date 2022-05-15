package com.vinga129.savolax.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.databinding.FragmentPostBinding;
import com.vinga129.savolax.ui.BaseFragment;
import com.vinga129.savolax.ui.BindingUtils;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

public class PostFragment extends BaseFragment {

    private PostViewModel mViewModel;
    private FragmentPostBinding binding;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PostViewModel postViewModel =
                new ViewModelProvider(main).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        binding.setViewmodel(postViewModel);
        binding.setFragment(this);
        binding.setBindingUtil(new BindingUtils());
        View root = binding.getRoot();
        view = root;

        binding.setViewmodel(postViewModel);

        return root;
    }

    public void navigateToPostUser() {
        /*ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(1); // post customer id
        Navigation.findNavController(view).navigate(PostFragmentDirections.toPostProfile());*/
    }

    public void showLikesWindow() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}