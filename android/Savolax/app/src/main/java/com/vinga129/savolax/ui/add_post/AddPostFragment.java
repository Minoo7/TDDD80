package com.vinga129.savolax.ui.add_post;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinga129.savolax.databinding.FragmentAddPostBinding;

public class AddPostFragment extends Fragment {

    private FragmentAddPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddPostViewModel addPostViewModel = new ViewModelProvider(this).get(AddPostViewModel.class);

        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPost;
        addPostViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}