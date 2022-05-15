package com.vinga129.savolax.ui.profile.post_preview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.MobileNavigationDirections;
import com.vinga129.savolax.databinding.PostPreviewItemBinding;
import com.vinga129.savolax.ui.post.PostFragment;
import com.vinga129.savolax.ui.post.PostFragmentDirections;
import com.vinga129.savolax.ui.post.PostViewModel;
import com.vinga129.savolax.ui.profile.BaseRecyclerAdapter;
import com.vinga129.savolax.ui.profile.ProfileFragment;
import com.vinga129.savolax.ui.profile.ProfileFragmentDirections;
import com.vinga129.savolax.ui.profile.ProfileViewModel;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import java.util.ArrayList;
import java.util.List;

public class PostPreviewsRecyclerAdapter extends BaseRecyclerAdapter {
    private OnItemListener mOnItemListener;
    PostPreviewItemBinding binding;
    // ProfileFragment profileFragment;

    /*public PostPreviewsRecyclerAdapter(List<Object> dataList, @Nullable OnItemListener onItemListener) {
        super(dataList);
        mOnItemListener = onItemListener;
    }*/

    public PostPreviewsRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>());
        mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewBinding(@NonNull ViewGroup parent) {
        binding = PostPreviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onViewHolderBind(ViewHolder viewHolder, int position, Object data) {
        Post post = (Post) data;
        binding.setPost(post);

        binding.layoutPost.setOnClickListener((View) -> {
            mOnItemListener.onItemClick(position, post);
        });
    }

    public interface OnItemListener {
        void onItemClick(int pos, Object data);
    }
}
