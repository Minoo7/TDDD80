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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vinga129.a3.retro.RetroGroup;
import com.vinga129.a3.retro.RetroUserList;
import com.vinga129.a3.retro.RetrofitClient;
import com.vinga129.a3.retro.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFragment extends Fragment implements MyAdapter.OnItemListener {

    private boolean isTablet;
    private boolean isLandscape;
    private Consumer<String> onClickSend;
    private InfoViewModel model;
    private List<String> items;
    private int mColumnCount;

    private static final String TAG = "logger";

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
        adapt(view);

        UserService service = RetrofitClient.getRetrofitInstance().create(UserService.class);
        Call<RetroGroup> listCall = service.getGroups();
        ItemFragment self = this;
        listCall.enqueue(new Callback<RetroGroup>() {

            @Override
            public void onResponse(Call<RetroGroup> call, Response<RetroGroup> response) {
                RetroGroup body = response.body();
                Log.d(TAG, "" + body);
                if (body != null) {
                    items = Arrays.asList(body.getGroups());
                    Methods.initRecyclerView(view, mColumnCount, new MyAdapter(items, self));
                }
            }

            @Override
            public void onFailure(Call<RetroGroup> call, Throwable t) {

            }
        });
    }

    private void adapt(View view) {
        isTablet = view.getResources().getBoolean(R.bool.isTablet);
        isLandscape = view.getResources().getBoolean(R.bool.isLandscape);
        if (isTablet || isLandscape) {
            mColumnCount = 2;
            model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
            onClickSend = model::setItem;
        } else {
            mColumnCount = 1;
            onClickSend = (item) -> {
                Navigation.findNavController(view).navigate(ItemFragmentDirections.navigateToInfoFragment(item));
            };
        }
    }

    @Override
    public void onItemClick(int pos) {
        onClickSend.accept(items.get(pos));
    }
}

