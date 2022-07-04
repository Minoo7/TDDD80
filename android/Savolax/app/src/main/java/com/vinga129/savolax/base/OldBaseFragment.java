package com.vinga129.savolax.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.ui.later.ActiveCustomerViewModel;
import com.vinga129.savolax.ui.later.UserRepository;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.retrofit.RestAPI;

public abstract class OldBaseFragment extends Fragment {
    //protected MainActivity main; //= (MainActivity) getActivity();
    protected NavController navController;
    protected Controller controller;
    protected RestAPI restAPI;
    protected ActiveCustomerViewModel user;
    protected UserRepository current;
    // Objects.requireNonNull(userRepository.getUserLiveData().getValue()).getId()


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //main = (MainActivity) getActivity();
        navController = ((MainActivity) requireActivity()).getNavController();
        controller = Controller.getInstance();
        restAPI = Controller.getInstance().getRestAPI();
        user = new ViewModelProvider(requireActivity()).get(ActiveCustomerViewModel.class);
        current = UserRepository.getINSTANCE();
    }
}
