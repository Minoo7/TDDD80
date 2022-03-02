package com.vinga129.lec2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainFragment extends Fragment {

    EditText input;
    InfoViewModel model;

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

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
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        v.findViewById(R.id.inputButton).setOnClickListener((View) -> updateUI(v));
        input = v.findViewById(R.id.input);
        model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        return v;
    }

    private void updateUI(View v) {
        String data = input.getText().toString();
        Bundle result = new Bundle();
        result.putString("data", data);
        getParentFragmentManager().setFragmentResult("infoFromMain", result);
        // getParentFragmentManager().setFragmentResult("dataFromMain", result);
        model.setItem(data);
    }
}
