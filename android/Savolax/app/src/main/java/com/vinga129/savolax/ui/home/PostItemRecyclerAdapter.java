package com.vinga129.savolax.ui.home;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.Nullable;
import com.airbnb.paris.Paris;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.PostItemBinding;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

@AnnotationContentId(contentId = R.layout.post_item)
public class PostItemRecyclerAdapter extends BaseRecyclerAdapter<PostItemBinding> {

    public PostItemRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>(), onItemListener);
    }

    @Override
    public void onViewHolderBind(ViewHolder viewHolder, int position, Object data) {
        Post post = (Post) data;
        binding.setPost(post);

        if (post.getImageUrl() == null) {
            // binding.layoutPost.setLayoutH
            //Paris.styleBuilder(binding.layoutPost).layoutHeight(LayoutParams.WRAP_CONTENT);
            //etLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            binding.layoutPost.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

        binding.cardPost.setOnClickListener(__ -> onItemListener.onItemClick(position, post));
    }
}
