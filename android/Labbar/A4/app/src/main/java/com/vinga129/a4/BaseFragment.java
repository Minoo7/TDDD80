package com.vinga129.a4;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    MainActivity main;
    View view;
    InfoViewModel model;
    NavController navController;
    static final String TAG = "myLogger";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(getLayout(), parent,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        main = ((MainActivity) requireActivity());
        this.view = view;
        model = main.infoViewModel;
        navController = Navigation.findNavController(view);
        init();
    }

    public abstract int getLayout();

    public abstract void init();
}