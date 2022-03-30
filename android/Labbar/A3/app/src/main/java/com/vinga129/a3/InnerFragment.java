package com.vinga129.a3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.vinga129.a3.retro.RetroUserList;
import com.vinga129.a3.retro.RetrofitClient;
import com.vinga129.a3.retro.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InnerFragment extends Fragment {
    private View view;
    private boolean isTablet;
    private boolean isLandscape;
    private final String SAVED_GROUP = "savedGroup";
    private String group = "";
    private Button backBtn;
    private Bundle saved;
    private boolean activated = false;
    private final int mColumnCount = 1;

    private Runnable changeId;

    private static final String TAG = "logger";

    public InnerFragment() {
        // Required empty public constructor
    }

    /*public static InnerFragment newInstance() {
        return new InnerFragment();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getString(SAVED_GROUP) != null)
            saved = savedInstanceState;
    }

    private void init() {
        backBtn = view.findViewById(R.id.backBtn);
        if (saved != null) {
            group = saved.getString(SAVED_GROUP);
            activated = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_inner_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        init();
        adapt();
    }

    private void deActivate() {
        activated = false;
        // do reset text on inner list items..
    }

    private void activate(RetroUserList items) {
        activated = true;
        Methods.initRecyclerView(view.findViewById(R.id.innerlist), mColumnCount, new InnerListAdapter(items));
    }

    private void adapt() {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLandscape = view.getResources().getBoolean(R.bool.isLandscape);

        if (isTablet || isLandscape) {
            if (saved == null) {
                InfoViewModel model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
                model.getSelectedItem().observe(getViewLifecycleOwner(), (item) -> {
                    group = item;
                    getInfoWithNetworkCall();
                });
            }
            setOnBackListener(this::deActivate);
        } else {
            if (getArguments() != null) {
                group = InnerFragmentArgs.fromBundle(getArguments()).getItem();
                if (backBtn.getVisibility() == View.INVISIBLE)
                    backBtn.setVisibility(View.VISIBLE);
                //setText(item.getContent(), item.getDetails());
                getInfoWithNetworkCall();
                Runnable goBack = () -> Navigation.findNavController(view).navigate(R.id.navigateBackToListFragment);
                backBtn.setOnClickListener((View) -> goBack.run());
                setOnBackListener(goBack);
            }
        }
    }

    private void getInfoWithNetworkCall() {
        UserService service = RetrofitClient.getRetrofitInstance().create(UserService.class);
        Call<RetroUserList> listCall = service.getMembers(group);
        listCall.enqueue(new Callback<RetroUserList>() {
            @Override
            public void onResponse(Call<RetroUserList> call, Response<RetroUserList> response) {
                RetroUserList body = response.body();
                if (body != null)
                    if (!body.getItems().isEmpty())
                        activate(body);
            }

            @Override
            public void onFailure(Call<RetroUserList> call, Throwable t) {

            }
        });
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
        // Save the user's current state
        //if (content != null && content.getText() != null && content.getText() != "") {
        if (activated) {
            savedInstanceState.putString(SAVED_GROUP, group);

            // Always call the superclass so it can save the view hierarchy state
            super.onSaveInstanceState(savedInstanceState);
        }
    }
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