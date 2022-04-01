package com.vinga129.a2;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;


public class InnerFragment extends Fragment {
    private boolean isTablet;
    private boolean isLandscape;
    private final String CONTENT_TEXT = "contentText";
    private final String DETAILS_TEXT = "detailsText";
    private TextView content;
    private TextView details;
    private Button backBtn;
    private Bundle saved;
    private boolean empty;
    private InfoViewModel model;

    private Runnable changeId;

    private static final String TAG = "logger";

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

        if (savedInstanceState != null && savedInstanceState.getString(CONTENT_TEXT) != null && savedInstanceState.getString(DETAILS_TEXT) != null) {
            saved = savedInstanceState;
        }
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
        model = ((MainActivity) requireActivity()).dataViewModel;
        init(view);
        adapt(view);
        if (saved != null)
            setText(saved.getString(CONTENT_TEXT), saved.getString(DETAILS_TEXT));
        Log.d(TAG, "Get: " + content.getText());
    }

    private void adapt(View view) {
        isTablet = view.getResources().getBoolean(R.bool.isTablet);
        isLandscape = view.getResources().getBoolean(R.bool.isLandscape);

        if (isTablet || isLandscape) {
            model.getSelectedItem().observe(getViewLifecycleOwner(), (item) -> setText(item.getContent(), item.getDetails()));
            //}
            if (saved != null) {
                setText(saved.getString(CONTENT_TEXT), saved.getString(DETAILS_TEXT));
                //saved = null;
            }
            //setOnBackListener(() -> setText("", ""));
        } else {
            if (getArguments() != null) {
                GroupsContent.GroupItem item = InnerFragmentArgs.fromBundle(getArguments()).getItem();
                if (backBtn.getVisibility() == View.INVISIBLE)
                    backBtn.setVisibility(View.VISIBLE);
                setText(item.getContent(), item.getDetails());
                Runnable goBack = () -> Navigation.findNavController(view).navigate(R.id.navigateBackToListFragment);
                backBtn.setOnClickListener((View) -> goBack.run());
                setOnBackListener(goBack);
            }
        }
    }

    private void setText(String contentText, String detailsText) {
        content.setText(contentText);
        details.setText(detailsText);
    }

    private void init(View view) {
        content = view.findViewById(R.id.contentText);
        details = view.findViewById(R.id.detailsText);
        backBtn = view.findViewById(R.id.backBtn);
        if (saved != null)
            setText(saved.getString(CONTENT_TEXT), saved.getString(DETAILS_TEXT));
    }

    private void setOnBackListener(Runnable runnable) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        ((MainActivity) getActivity()).setOnBackListener(() -> {
            runnable.run();
            mainActivity.setOnBackListener(null);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "Saving: " );

        // Save the user's current state
        if (content != null && content.getText() != null && content.getText() != "") {
            savedInstanceState.putString(CONTENT_TEXT, content.getText().toString());
            savedInstanceState.putString(DETAILS_TEXT, details.getText().toString());

            // Always call the superclass so it can save the view hierarchy state
            super.onSaveInstanceState(savedInstanceState);
        }
    }

    //@Override
    //onBackPress = () -> s
    /*public void onBackPress() {
        Log.d("logmsg", "onBackPress: ");
        content.setText("");
        details.setText("");
    }*/

    /*private void goBack() {
        if (!(isTablet || isLandscape))


        /* Old:
        getParentFragmentManager().popBackStackImmediate();*/
    //}*/

    /**
     * Supplier       ()    -> x
     * Consumer       x     -> ()
     * BiConsumer     x, y  -> ()
     * Callable       ()    -> x throws ex
     * Runnable       ()    -> ()
     * Function       x     -> y
     * BiFunction     x,y   -> z
     * Predicate      x     -> boolean
     * UnaryOperator  x1    -> x2
     * BinaryOperator x1,x2 -> x3
     * */
}