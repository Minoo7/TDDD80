package com.vinga129.a2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment {

    InfoViewModel model;

    public GroupsFragment() {
        // Required empty public constructor
    }


    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
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
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String itemsTagName = view.getResources().getString(R.string.itemsTag);

        for (TextView item : ((List<TextView>)listBuilder(view.findViewById(R.id.itemsLinearLayout), itemsTagName))) {
            item.setOnClickListener((View) -> viewDetails(view, item));
        }
    }

    private void viewDetails(View view, TextView textView) {
        final NavController navController = Navigation.findNavController(view);
        GroupsFragmentDirections.NavigateToInfoFragment action = GroupsFragmentDirections.navigateToInfoFragment();
        action.setTopic(textView.getText().toString());
        navController.navigate(action);
    }

    public ArrayList listBuilder(ViewGroup parent, String tagName) {
        ArrayList<View> items = new ArrayList<>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getTag() != null && child.getTag().toString() == tagName) {
                items.add(((TextView)child));
            }
        }
        return items;
    }

    /**
     * Old
     * */
    /*
    item.setOnClickListener((View) -> enterView(item));
    model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);

        private void enterView(TextView textView) { // Old way
        String data = textView.getText().toString();
        Bundle result = new Bundle();
        result.putString("data", "");
        getParentFragmentManager().setFragmentResult("infoFromMain", result);
        model.setItem(data);
    }
     */
}