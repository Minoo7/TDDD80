package com.vinga129.savolax.base;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, B extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseRecyclerAdapter<T, B>.ViewHolder> {

    protected List<T> dataList;
    protected B binding;
    protected int contentId;
    protected final OnItemListener onItemListener;

    public BaseRecyclerAdapter(List<T> dataList, @Nullable OnItemListener onItemListener) {
        this.dataList = dataList;
        if (contentId == 0)
            contentId = AnnotationUtil.check(this);
        this.onItemListener = onItemListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> dataList) {
        this.dataList = dataList != null ? dataList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (dataList != null) {
            dataList.add(data);
            notifyItemInserted(dataList.size() - 1);
        }
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