package com.vinga129.savolax.ui.post;

import static com.vinga129.savolax.util.BindingUtils.changeBackgroundTint;

import android.graphics.Color;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentPostBinding;
import com.vinga129.savolax.retrofit.rest_objects.Post;

@AnnotationContentId(contentId = R.layout.fragment_post)
public class PostFragment extends BaseFragment<FragmentPostBinding> {

    private PostViewModel postViewModel;

    @Override
    protected void initFragment() {
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

        binding.setViewmodel(postViewModel);

        Post post = PostFragmentArgs.fromBundle(getArguments()).getPost();

        binding.setPost(post);
        binding.setFragment(this);

        postViewModel.loadMiniCustomer(post.getCustomerId());
        //postViewModel
          //      .initLikeValue(post.getLikes().stream().anyMatch(mini -> mini.getId() == user.getId()), post.getId());

        // binding.setBindingUtil(new BindingUtils());

    }

    public void showLikesWindow() {

    }

    public CompoundButton.OnCheckedChangeListener likeButtonAction = ((compoundButton, checked) -> {
        if (checked)
            changeBackgroundTint(requireContext(), compoundButton, R.color.orange);
        else
            changeBackgroundTint(compoundButton, Color.GRAY);
    });

    public void navigateToPostUser() {
        /*ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(1); // post customer id
        Navigation.findNavController(view).navigate(PostFragmentDirections.toPostProfile());*/
    }
}