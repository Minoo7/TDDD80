package com.vinga129.savolax.ui.comments;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.CommentItemBinding;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import java.util.ArrayList;

@SuppressLint("NonConstantResourceId")
@AnnotationContentId(contentId = R.layout.comment_item)
public class CommentRecyclerAdapter extends BaseRecyclerAdapter<CommentItemBinding> {

    public CommentRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>(), onItemListener);
    }

    @Override
    public void onViewHolderBind(final ViewHolder viewHolder, final int position, final Object data) {
        Comment comment = (Comment) data;
        binding.setComment(comment);

        /*binding.commentUsername.post(() -> {
            SpannableString s = new SpannableString(comment.getContent());
            int leftMargin = binding.commentUsername.getWidth() + 20;
            s.setSpan(new LeadingMarginSpan.Standard(leftMargin, 0), 0, 1, 0);
            binding.commentText.setText(s);
        });

        // meaning it is a new comment and the code above does not work
        if (comment.getCreated_at() == null) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.commentText
                    .getLayoutParams();
            params.startToEnd = R.id.comment_username;
            params.leftMargin = 8;
            binding.commentText.requestLayout();
        }*/

        //contentView.setText(Html.fromHtml("<font color=\'black\'>" + username + "</font> &mdash; " + TextUtils.htmlEncode(content));

        binding.commentText.setText(HtmlCompat.fromHtml(
                "<font color=\'black\'>" + comment.getCustomer().getUsername() + "</font> "
                        + TextUtils.htmlEncode(comment.getContent()), HtmlCompat.FROM_HTML_MODE_LEGACY));

    }
}
