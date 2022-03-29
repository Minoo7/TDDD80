package com.vinga129.a3;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinga129.a3.databinding.FragmentItemBinding;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final List<GroupsContent.GroupItem> mValues;
    private final OnItemListener mOnItemListener;

    public MyAdapter(List<GroupsContent.GroupItem> items, OnItemListener onItemListener) {
        mValues = items;
        this.mOnItemListener = onItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list, parent, false);
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mOnItemListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mContentView;
        public GroupsContent.GroupItem mItem;
        OnItemListener onItemListener;

        public ViewHolder(FragmentItemBinding binding, OnItemListener onItemListener) {
            super(binding.getRoot());
            mContentView = binding.content;
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getBindingAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int pos);
    }
}