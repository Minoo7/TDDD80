package com.vinga129.a3;

import android.content.Context;
import android.os.Bundle;
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
        listCall.enqueue(new Callback<RetroGroup>() {
            @Override
            public void onResponse(Call<RetroGroup> call, Response<RetroGroup> response) {
                RetroGroup body = response.body();
                if (body != null) {
                    items = Arrays.asList(body.getGroups());
                    initRecyclerView(view);
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

    private void initRecyclerView(View view) {
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyAdapter(items, this));
        }
    }

    @Override
    public void onItemClick(int pos) {
       //GroupsContent.GroupItem item = items.get(pos);
        onClickSend.accept(items.get(pos));
    }

    /*Previous:

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupsContent = new GroupsContent(getActivity().getResources());
        initRecyclerView(view);
        model = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        Consumer<TextView> method = (((ViewGroup) getView().getParent()).findViewById(R.id.placeholder) == null) ? (textView) -> viewDetails(textView) : (textView) -> viewDetailsNav(view, textView);
        String itemsTagName = view.getResources().getString(R.string.itemsTag);
        ArrayList<TextView> items = listBuilder(view.findViewById(R.id.itemsLinearLayout), itemsTagName);
        items.forEach(item -> item.setOnClickListener((View) -> method.accept(item)));
    }



    // for phone size
    private void viewDetailsNav(View view, TextView textView) {
        final NavController navController = Navigation.findNavController(view);
        //String s = getResources().getString(R.string.tv);
        //GroupsFragmentDirections.NavigateToInfoFragment action = GroupsFragmentDirections.navigateToInfoFragment();
        //action.setTopic(textView.getText().toString());
        //action.setDetails()
        List list = new ArrayList<>();
        //getResources().getString(R.string.finland);
        //list.add(getResources().getString(getResources().getIdentifier(ntpServers[i][0], "string", getPackageName())));
        //navController.navigate(action);
    }

    private ArrayList<TextView> listBuilder(ViewGroup parent, String tagName) {
        ArrayList<TextView> items = new ArrayList<>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getTag() != null && child.getTag().toString().equals(tagName)) {
                items.add(((TextView) child));
            }
        }
        return items;
    }

    /*
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

