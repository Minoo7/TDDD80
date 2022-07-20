package com.vinga129.savolax.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.ui.later.UserRepository;

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment {

    private int contentId;
    protected Bundle bundle;
    protected Activity activity;
    protected B binding;
    protected NavController navController;
    protected UserRepository user;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (contentId == 0) {
            bundle = getArguments();
            contentId = AnnotationUtil.check(this);
            activity = requireActivity();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, contentId, container, false);

            if (activity instanceof MainActivity)
                navController = ((MainActivity) activity).getNavController();
            user = UserRepository.getINSTANCE();

            initFragment();
        }

        return binding.getRoot();
    }

    protected abstract void initFragment();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}