package com.vinga129.savolax.ui.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.paris.Paris;
import com.airbnb.paris.StyleBuilder;
import com.airbnb.paris.annotations.Style;
import com.airbnb.paris.annotations.Styleable;
import com.vinga129.savolax.R;
import com.vinga129.savolax.databinding.FragmentPostBinding;
import com.vinga129.savolax.ui.ActiveCustomerViewModel;
import com.vinga129.savolax.ui.BaseFragment;
import com.vinga129.savolax.ui.BindingUtils;
import com.vinga129.savolax.ui.CreatorUtil;
import com.vinga129.savolax.ui.profile.post_preview.PostPreviewsRecyclerAdapter;
import com.vinga129.savolax.ui.retrofit.rest_objects.Like;
import com.vinga129.savolax.ui.retrofit.rest_objects.Post;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class PostFragment extends BaseFragment {

    private PostViewModel mViewModel;
    private FragmentPostBinding binding;
    private PostViewModel postViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        binding.setViewmodel(postViewModel);
        binding.setFragment(this);
        binding.setBindingUtil(new BindingUtils());
        binding.setCurrent(current);
        View root = binding.getRoot();

        System.out.println("yoo");
        postViewModel.getPost().observe(getViewLifecycleOwner(), post -> {
            for (Like like : post.getLikes()) {
                System.out.println(like.getCustomer_id());
            }
            System.out.println(post.getLikes().stream().anyMatch(mini -> mini.getId() == 1));
        });

        return root;
    }


    public void navigateToPostUser() {
        /*ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(1); // post customer id
        Navigation.findNavController(view).navigate(PostFragmentDirections.toPostProfile());*/
    }

    public void showLikesWindow() {

    }

    public void likePost() {
        //ToggleButton toggle = new ToggleButton();
        /*CompoundButton.OnCheckedChangeListener d = (compoundButton, checked) -> {
            if (checked) {

            }
            else {

            }
        };
        toggle.setOnCheckedChangeListener(d);*/
    }

    /**
     * ADD SAMLAD PLATS FÖR NÄTVERKS REUQESTS SOM KAN AUTO SKICKA TILLBAKA HANTERING AV ERRORS*/


    public CompoundButton.OnCheckedChangeListener likeButtonAction = ((compoundButton, checked) -> {
        if (checked) {
            int post_id = Objects.requireNonNull(postViewModel.getPost().getValue()).getId();
            restAPI.likePost(post_id).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        //postViewModel.getPost().
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {

                }
            }); // customer_id
            BindingUtils.changeBackgroundTint(requireContext(), compoundButton, R.color.orange);
        } else {
            int like_id = postViewModel.getPost().getValue().getLikes().stream().filter(a -> a.getCustomer_id() == current.getId()).collect(Collectors.toList()).get(0).getId();
            restAPI.deleteLike(postViewModel.getPost().getValue().getId(), like_id).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {

                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {

                }
            });
            BindingUtils.changeBackgroundTint(compoundButton, Color.GRAY);
        }
    });

    /*public Consumer<View> likeButtonAction = (view) -> {
        // view.setBackgroundColor(main.getResources().getColor(R.color.black));
        Button b = (Button) view;
        // b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        Paris.styleBuilder(view)
                .backgroundTint(ContextCompat.getColor(main, R.color.orange)).apply();
    };*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}