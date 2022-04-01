package com.vinga129.a3;

import android.os.Bundle;
import android.util.Log;
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


public class InnerFragment extends Fragment implements NetworkReceiver {
    private View view;
    private MainActivity mainActivity;
    private InfoViewModel model;
    private InnerListAdapter adapter;
    private Button backBtn;
    private boolean isTablet;
    private boolean isLandscape;

    public InnerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        model.getSelectedItem().observe(getViewLifecycleOwner(), (item) ->
                mainActivity.doNetworkCall(this, mainActivity.service.getMembers(item)));
    }

    private void init() {
        mainActivity = ((MainActivity) requireActivity());
        model = mainActivity.dataViewModel;
        backBtn = view.findViewById(R.id.backBtn);
    }

    private void adapt() {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLandscape = view.getResources().getBoolean(R.bool.isLandscape);

        if (isTablet || isLandscape)
            mainActivity.setOnBackListener(() -> adapter.clearItems());
        else {
            if (backBtn.getVisibility() == View.GONE)
                backBtn.setVisibility(View.VISIBLE);
            Runnable goBack = () -> Navigation.findNavController(view).navigate(R.id.navigateBackToListFragment);
            backBtn.setOnClickListener((View) -> goBack.run());
            mainActivity.setOnBackListener(() -> {
                goBack.run();
                mainActivity.setOnBackListener(null);
            });
        }
    }

    @Override
    public <T> void onNetworkReceived(T body) {
        RetroUserList retroUserList = (RetroUserList) body;
        if (!retroUserList.getItems().isEmpty()) {
            adapter = new InnerListAdapter(retroUserList);
            Methods.initRecyclerView(view.findViewById(R.id.innerlist), 1, adapter);
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