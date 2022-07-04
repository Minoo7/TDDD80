package com.vinga129.savolax.ui.products;

import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentProductsBinding;

@AnnotationContentId(contentId = R.layout.fragment_products)
public class ProductsFragment extends BaseFragment<FragmentProductsBinding> {

    @Override
    protected void initFragment() {
        ProductsViewModel productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        //final TextView textView = binding.textProducts;
        //productsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.test_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());

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

        binding.buttonItem1.setOnClickListener((View) -> {
            ActionMode actionMode = requireActivity().startActionMode(callback);
            actionMode.setTitle("1 selected");
        });
    }


}