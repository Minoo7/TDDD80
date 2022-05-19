package com.vinga129.savolax.ui.products;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinga129.savolax.databinding.FragmentProductsBinding;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ProductsViewModel productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textProducts;
        //productsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}