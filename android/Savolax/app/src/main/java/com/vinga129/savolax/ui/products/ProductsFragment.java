package com.vinga129.savolax.ui.products;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentProductsBinding;

@AnnotationContentId(contentId = R.layout.fragment_products)
public class ProductsFragment extends BaseFragment<FragmentProductsBinding> {

    @Override
    protected void initFragment() {
        ProductsViewModel productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        ActionMode.Callback callback = new Callback() {
            @Override
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                requireActivity().getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                /*switch(menuItem.getItemId()) {
                    case (R.id.share):
                        return true;
                }
                return false;*/

                return true;

            }

            @Override
            public void onDestroyActionMode(final ActionMode actionMode) {

            }
        };

        binding.buttonItem1.setOnClickListener((View) ->
                ((MainActivity) requireActivity()).setCallBackWithTitle(callback, "1 selected"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).finishActionMode();
    }
}