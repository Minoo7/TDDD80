package com.vinga129.savolax.ui.other;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinga129.savolax.databinding.FragmentOtherBinding;

public class OtherFragment extends Fragment {

    private FragmentOtherBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OtherViewModel otherViewModel =
                new ViewModelProvider(this).get(OtherViewModel.class);

        binding = FragmentOtherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textOther;
        //otherViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}