package com.vinga129.savolax.ui.post;

import static com.vinga129.savolax.util.BindingUtils.changeBackgroundTint;

import android.graphics.Color;
import android.widget.CompoundButton;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentPostBinding;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import java.util.List;

@AnnotationContentId(contentId = R.layout.fragment_post)
public class PostFragment extends BaseFragment<FragmentPostBinding> {

    private PostViewModel postViewModel;
    private Post post;

    @Override
    protected void initFragment() {
        post = PostFragmentArgs.fromBundle(getArguments()).getPost();

        postViewModel = new PostViewModel(post.getCustomerId(), post.getLikes().size());

        binding.setViewmodel(postViewModel);

        binding.setPost(post);
        binding.setFragment(this);

        postViewModel
                .initLikeValue(hasLikedPost(), post.getId());
        System.out.println("kabbaaaa");
        System.out.println(post.getId());
        System.out.println(hasLikedPost());
        System.out.println(post.getLikes());
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

    public void navigateToPostComments() {
        navController.navigate(PostFragmentDirections.toComments(post.getComments().toArray(new Comment[0]), post.getId()));
    }

    public boolean hasLikedPost() {
        return post.isLikedBy(user.getId());
    }
}