package com.vinga129.savolax.ui.comments;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentCommentsBinding;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;

@SuppressLint("NonConstantResourceId")
@AnnotationContentId(contentId = R.layout.fragment_comments)
public class CommentsFragment extends BaseFragment<FragmentCommentsBinding> {

    private CommentsViewModel commentsViewModel;
    private CommentRecyclerAdapter adapter;

    @Override
    protected void initFragment() {
        int postId = CommentsFragmentArgs.fromBundle(getArguments()).getPostId();
        List<Comment> comments = Arrays.asList(CommentsFragmentArgs.fromBundle(getArguments()).getComments());

        commentsViewModel = new CommentsViewModel(postId, comments);
        adapter = new CommentRecyclerAdapter(null);

        binding.setViewmodel(commentsViewModel);
        binding.setAdapter(adapter);
        binding.setFragment(this);
    }

    public void addComment() {
        String commentText = binding.commentNew.getText().toString().trim();

        if (!commentText.equals("")) {
            Comment comment = new Comment(commentText);
            commentsViewModel.addComment(comment);
            showCommentInList(comment);
        }
    }

    private void showCommentInList(Comment comment) {
        Controller.getInstance().getRestAPI().getCustomerAsMini(user.getId()).doOnSuccess(miniCustomer -> {
            comment.setCustomer(miniCustomer);
            requireActivity().runOnUiThread(() -> adapter.addData(comment));
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}