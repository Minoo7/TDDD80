package com.vinga129.a2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InnerFragment extends Fragment {
    InfoViewModel model;
    TextView header;
    public InnerFragment() {
        // Required empty public constructor
    }

    public static InnerFragment newInstance() {
        InnerFragment fragment = new InnerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_inner, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header = view.findViewById(R.id.headerTextView);
        if (((ViewGroup) getView().getParent()).findViewById(R.id.placeholder) != null) { // phone
            String topicText = InnerFragmentArgs.fromBundle(getArguments()).getTopic();
            header.setText(topicText);
            view.findViewById(R.id.backBtn).setOnClickListener((View) -> goBack(view));
        }
        else { // tablet
            model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
            model.getSelectedItem().observe(getViewLifecycleOwner(), data -> header.setText(data));
        }
    }



    private void goBack(View view) {
        Navigation.findNavController(view).navigate(R.id.navigateBackToListFragment);

        /* Old:
        getParentFragmentManager().popBackStackImmediate();*/
    }
}