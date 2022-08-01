package com.vinga129.savolax.other;

import androidx.annotation.Nullable;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.databinding.MiniCustomerItemBinding;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import java.util.ArrayList;

@AnnotationContentId(contentId = R.layout.mini_customer_item)
public class MiniCustomersRecyclerAdapter extends BaseRecyclerAdapter<MiniCustomer, MiniCustomerItemBinding> {

    public MiniCustomersRecyclerAdapter(@Nullable OnItemListener onItemListener) {
        super(new ArrayList<>(), onItemListener);
    }

    @Override
    public void onViewHolderBind(final ViewHolder viewHolder, final int position, final Object data) {
        MiniCustomer customer = (MiniCustomer) data;
        binding.setMiniCustomer(customer);

        binding.clickableMinicustomer.setOnClickListener(__ -> {
            if (onItemListener != null)
                onItemListener.onItemClick(position, data);
        });
    }
}
