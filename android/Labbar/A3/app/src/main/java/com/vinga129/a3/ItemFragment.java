package com.vinga129.a3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.vinga129.a3.retro.RetroGroup;
import com.vinga129.a3.retro.RetrofitClient;
import com.vinga129.a3.retro.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFragment extends Fragment implements MyAdapter.OnItemListener, NetworkReceiver {

    private View view;
    private boolean isTablet;
    private boolean isLandscape;
    private Consumer<String> onClickSend;
    private List<String> items;
    private int mColumnCount;
    private MainActivity mainActivity;

    public ItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mainActivity = ((MainActivity) requireActivity());
        adapt(view);

        mainActivity.doNetworkCall(this, mainActivity.service.getGroups());
    }

    private void adapt(View view) {
        isTablet = view.getResources().getBoolean(R.bool.isTablet);
        isLandscape = view.getResources().getBoolean(R.bool.isLandscape);
        if (isTablet || isLandscape) {
            mColumnCount = 2;
            onClickSend = this::setModelItem;
        } else {
            mColumnCount = 1;
            onClickSend = (item) -> {
                setModelItem(item);
                Navigation.findNavController(view).navigate(ItemFragmentDirections.navigateToInfoFragment());
            };
        }
    }

    private void setModelItem(String item) {
        mainActivity.dataViewModel.setItem(item);
    }

    @Override
    public void onItemClick(int pos) {
        onClickSend.accept(items.get(pos));
    }

    @Override
    public <T> void onNetworkReceived(T body) {
        RetroGroup retroGroup = (RetroGroup) body;
        items = Arrays.asList((String[]) retroGroup.getGroups());
        Methods.initRecyclerView(view, mColumnCount, new MyAdapter(items, this));
    }
}

