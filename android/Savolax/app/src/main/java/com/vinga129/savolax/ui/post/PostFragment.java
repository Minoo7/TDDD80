package com.vinga129.savolax.ui.post;

import static com.vinga129.savolax.util.HelperUtil.moveToProfileAction;

import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentPostBinding;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import com.vinga129.savolax.retrofit.rest_objects.Post;

@AnnotationContentId(contentId = R.layout.fragment_post)
public class PostFragment extends BaseFragment<FragmentPostBinding> {

    private Post post;

    @Override
    protected void initFragment() {
        post = PostFragmentArgs.fromBundle(getArguments()).getPost();

        PostViewModel postViewModel = new PostViewModel(post.getLikes().size());

        binding.setViewmodel(postViewModel);

        binding.setPost(post);
        binding.setFragment(this);

        postViewModel
                .initLikeValue(hasLikedPost(), post.getId());
    }

    public void showLikesWindow() {

    }

    public void navigateToPostUser() {
        //fix multiple backstacks instead of popbackstack!
        navController.popBackStack();
        navController.navigate(moveToProfileAction(post.getCustomerId()));
    }

    public void navigateToPostComments() {
        navController.navigate(PostFragmentDirections.toComments(post.getComments().toArray(new Comment[0]), post.getId()));
    }

    public boolean hasLikedPost() {
        return post.isLikedBy(user.getId());
    }
}