package com.vinga129.savolax.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.NetworkReceiver;
import com.vinga129.savolax.databinding.FragmentProfileBinding;
import com.vinga129.savolax.ui.profile.post_preview.PostPreview;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.RestAPI;
import com.vinga129.savolax.ui.retrofit.rest_objects.CustomerProfile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements NetworkReceiver {

    private MainActivity mainActivity;

    private FragmentProfileBinding binding;

    private RecyclerView mRecyclerView;
    private List<Object> viewItems = new ArrayList<>();

    private PostPreviewsRecyclerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();

        System.out.println("yoo");
        System.out.println(mainActivity.restAPI);
        mainActivity.doNetworkCall(this, mainActivity.restAPI.getCustomerProfile("1"));

        /*mRecyclerView = binding.recyclerPostPreviews;

        viewItems.add(new PostPreview("New Year Day", "1st Jan Wednesday", "https://i.picsum.photos/id/278/536/354.jpg?hmac=B3RGgunW6oirJoQEgt80to9HNb7oZqLut-4fFVVc9NM"));

        mAdapter = new PostPreviewsRecyclerAdapter(viewItems, null);
        mRecyclerView.setAdapter(mAdapter);
        binding.textFollowersNumber.setText("2");*/
        //test();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public <T> void onNetworkReceived(T body) {
        CustomerProfile customerProfile = (CustomerProfile) body;

        System.out.println(body);

        System.out.println("customerProfile:" + customerProfile);

        if (customerProfile != null) {
            binding.textFollowers.setText(customerProfile.getFollowers().size());
            binding.textFollowers.setText("2");
        }
    }

    /*public void test() {
        RestAPI restAPI = Controller.getRetrofitInstance().create(RestAPI.class);
        // Call<CustomerProfile> call = restAPI.getCustomerProfile("1");
        Call call = restAPI.getTest();
        System.out.println("nah");
        call.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                System.out.println("YOOOOO");
            }

            @Override
            public void onFailure(Call<CustomerProfile> call, Throwable t) {
                System.out.println("call" +  call);
                System.out.println("didnt");
            }
        });

    }*/
}