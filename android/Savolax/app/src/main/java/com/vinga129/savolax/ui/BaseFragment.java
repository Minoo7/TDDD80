package com.vinga129.savolax.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.profile.ProfileViewModel;

public abstract class BaseFragment extends Fragment {
    public MainActivity main; //= (MainActivity) getActivity();

    /*public BaseFragment() {
        main = (MainActivity) getActivity();
    }*/

    /*public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();
    }
}
