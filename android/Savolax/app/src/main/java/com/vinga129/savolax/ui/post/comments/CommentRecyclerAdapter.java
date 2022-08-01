package com.vinga129.savolax.ui.post.comments;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.CommentItemBinding;
import com.vinga129.savolax.retrofit.rest_objects.Comment;
import com.vinga129.savolax.util.BindingUtils;
import java.util.ArrayList;
import java.util.Date;

@AnnotationContentId(contentId = R.layout.comment_item)
public class CommentRecyclerAdapter extends BaseRecyclerAdapter<Comment, CommentItemBinding> {

    public CommentRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>(), onItemListener);
    }

    @Override
    public void onViewHolderBind(final ViewHolder viewHolder, final int position, final Object data) {
        Comment comment = (Comment) data;

        if (comment.getCreated_at() == null)
            comment.setCreated_at(BindingUtils.myFormat.format(new Date()));

        binding.setComment(comment);

        binding.commentText.setText(HtmlCompat.fromHtml(
                "<font color='black'>" + comment.getCustomer().getUsername() + "</font> "
                        + TextUtils.htmlEncode(comment.getContent()), HtmlCompat.FROM_HTML_MODE_LEGACY));

    }
}
