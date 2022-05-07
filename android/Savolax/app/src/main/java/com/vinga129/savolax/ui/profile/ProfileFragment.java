package com.vinga129.savolax.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private RecyclerView mRecyclerView;
    private List<Object> viewItems = new ArrayList<>();

    // private RecyclerView.Adapter mAdapter;
    private PostPreviewsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // final TextView textView = binding.textAccount;
        // profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        mRecyclerView = binding.recyclerPostPreviews;

        // layoutManager = new GridLayoutManager(getContext(), 2);
        // mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new PostPreviewsRecyclerAdapter(viewItems, null);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}