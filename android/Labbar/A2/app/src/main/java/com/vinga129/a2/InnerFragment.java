package com.vinga129.a2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InnerFragment extends Fragment {
    private boolean isTablet;

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
        isTablet = view.getResources().getBoolean(R.bool.isTablet);
        TextView content = view.findViewById(R.id.contentText);
        TextView details = view.findViewById(R.id.detailsText);
        if (isTablet) {
            InfoViewModel model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
            model.getSelectedItem().observe(getViewLifecycleOwner(), content::setText);
            model.getItemDetails().observe(getViewLifecycleOwner(), details::setText);
        }
        else {
            InnerFragmentArgs args = InnerFragmentArgs.fromBundle(getArguments());
            content.setText(args.getTopic());
            details.setText(args.getDetails());
            view.findViewById(R.id.backBtn).setOnClickListener((View) -> goBack(view));
        }
    }

    private void goBack(View view) {
        Navigation.findNavController(view).navigate(R.id.navigateBackToListFragment);

        /* Old:
        getParentFragmentManager().popBackStackImmediate();*/
    }
}