package com.vinga129.savolax.base;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected List<Object> dataList;
    ViewBinding binding;

    public BaseRecyclerAdapter(List<Object> dataList) {
        this.dataList = dataList;
    }

    public void updateData(List<Object> dataList) {
        this.dataList = dataList != null ? dataList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return onCreateViewBinding(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onViewHolderBind(holder, position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    protected abstract ViewHolder onCreateViewBinding(@NonNull ViewGroup parent);

    public abstract void onViewHolderBind(ViewHolder viewHolder, int position, Object data);

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}