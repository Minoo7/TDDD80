package com.vinga129.savolax.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.databinding.FragmentHomeBinding;

@AnnotationContentId(contentId = R.layout.fragment_home)
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    protected void initFragment() {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}