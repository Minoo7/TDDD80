package com.vinga129.savolax.ui.profile;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentListOfCustomersBinding;
import com.vinga129.savolax.other.MiniCustomersRecyclerAdapter;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import java.util.Arrays;

@AnnotationContentId(contentId = R.layout.fragment_list_of_customers)
public class ListOfCustomersFragment extends BaseFragment<FragmentListOfCustomersBinding> {

    @Override
    protected void initFragment() {
        MiniCustomer[] customers = ListOfCustomersFragmentArgs.fromBundle(getArguments()).getCustomers();
        binding.setMiniCustomersAdapter(new MiniCustomersRecyclerAdapter(Arrays.asList(customers),
                (pos, data) -> navController.navigate(ListOfCustomersFragmentDirections.toProfile()
                        .setCustomerId(new CustomNullableIntegerArgument(((MiniCustomer) data).getId())))));
        ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}