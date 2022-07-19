package com.vinga129.savolax.base;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<B extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseRecyclerAdapter<B>.ViewHolder> {

    protected List<Object> dataList;
    protected B binding;
    protected int contentId;
    protected final OnItemListener onItemListener;

    public BaseRecyclerAdapter(List<Object> dataList, @Nullable OnItemListener onItemListener) {
        this.dataList = dataList;
        if (contentId == 0)
            contentId = AnnotationUtil.check(this);
        this.onItemListener = onItemListener;
    }

    public void updateData(List<Object> dataList) {
        this.dataList = dataList != null ? dataList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), contentId, parent, false);
        return new ViewHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onViewHolderBind(holder, position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public abstract void onViewHolderBind(ViewHolder viewHolder, int position, Object data);

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder() {
            super(binding.getRoot());
        }
    }

    public interface OnItemListener {
        void onItemClick(int pos, Object data);
    }
}