package com.vinga129.savolax.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.RestAPI;

import java.util.Objects;

public abstract class BaseFragment extends Fragment {
    //protected MainActivity main; //= (MainActivity) getActivity();
    protected NavController navController;
    protected Controller controller;
    protected RestAPI restAPI;
    protected ActiveCustomerViewModel user;
    protected UserRepository current;
    // Objects.requireNonNull(userRepository.getUserLiveData().getValue()).getId()

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
        //main = (MainActivity) getActivity();
        navController = ((MainActivity) requireActivity()).getNavController();
        controller = Controller.getInstance();
        restAPI = Controller.getInstance().getRestAPI();
        user = new ViewModelProvider(requireActivity()).get(ActiveCustomerViewModel.class);
        current = UserRepository.getINSTANCE();
    }
}
