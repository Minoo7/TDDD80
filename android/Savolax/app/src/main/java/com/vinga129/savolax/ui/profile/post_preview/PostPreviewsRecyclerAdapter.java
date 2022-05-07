package com.vinga129.savolax.ui.profile.post_preview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.vinga129.savolax.databinding.PostPreviewItemBinding;
import com.vinga129.savolax.ui.profile.BaseRecyclerAdapter;

import java.util.List;

public class PostPreviewsRecyclerAdapter extends BaseRecyclerAdapter {
    private final OnItemListener mOnItemListener;
    PostPreviewItemBinding binding;

    public PostPreviewsRecyclerAdapter(List<Object> dataList, @Nullable OnItemListener onItemListener) {
        super(dataList);
        mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewBinding(@NonNull ViewGroup parent) {
        // return new ViewHolder(FragmentProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(PostPreviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onViewHolderBind(ViewHolder viewHolder, int position, Object data) {
        PostPreview postPreview = (PostPreview) data;


        binding.textTitle.setText(postPreview.getTitle());
        binding.textContent.setText(postPreview.getContent());
        Picasso.get()
                .load(postPreview.getImageUrl())
                .into(binding.imagePost);
    }

    public interface OnItemListener {
        void onItemClick(int pos);
    }
}
