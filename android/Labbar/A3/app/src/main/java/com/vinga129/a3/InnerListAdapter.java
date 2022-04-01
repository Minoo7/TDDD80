package com.vinga129.a3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinga129.a3.databinding.FragmentInnerItemBinding;
import com.vinga129.a3.retro.RetroUserList;

import org.w3c.dom.Text;

import java.util.List;

public class InnerListAdapter extends RecyclerView.Adapter<InnerListAdapter.ViewHolder> {

    private final List<RetroUserList.RetroUser> mValues;

    public InnerListAdapter(RetroUserList items) {
        mValues = items.getItems();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list, parent, false);
        return new ViewHolder(FragmentInnerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RetroUserList.RetroUser item = holder.mItem = mValues.get(position);
        holder.mNameView.append(item.getName());
        holder.mEmailView.append(item.getEmail());
        if (item.getAnswered() != null)
            holder.mAnsweredView.append(item.getAnswered());
        else
            holder.mAnsweredView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void clearItems() {
        // only works once?*
        mValues.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mEmailView;
        public final TextView mAnsweredView;
        public RetroUserList.RetroUser mItem;

        public ViewHolder(FragmentInnerItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameText;
            mEmailView = binding.emailText;
            mAnsweredView = binding.answeredText;
            mNameView.setText(R.string.nameTextHeader);
            mEmailView.setText(R.string.emailTextHeader);
            mAnsweredView.setText(R.string.answeredTextHeader);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}