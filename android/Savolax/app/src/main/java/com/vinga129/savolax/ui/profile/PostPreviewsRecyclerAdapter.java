package com.vinga129.savolax.ui.profile;

import androidx.annotation.Nullable;


import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.PostPreviewItemBinding;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import java.util.ArrayList;

@AnnotationContentId(contentId = R.layout.post_preview_item)
public class PostPreviewsRecyclerAdapter extends BaseRecyclerAdapter<PostPreviewItemBinding> {

    public PostPreviewsRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>(), onItemListener);
    }

    @Override
    public void onViewHolderBind(ViewHolder viewHolder, int position, Object data) {
        Post post = (Post) data;
        binding.setPost(post);

        binding.layoutPost.setOnClickListener((View) -> {
            onItemListener.onItemClick(position, post);
        });
    }
}
