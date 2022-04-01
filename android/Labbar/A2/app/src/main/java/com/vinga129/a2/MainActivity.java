package com.vinga129.a2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OnBackListener onBackListener;
    public InfoViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (onBackListener != null)
            onBackListener.onBackPress();

        //getSupportFragmentManager().popBackStack();
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    @FunctionalInterface
    public interface OnBackListener {
        void onBackPress();
    }

    /**
     * Old*/
    /*
    GroupsFragment groupF;
    InnerFragment innerF;*/
    /*View placeholder = findViewById(R.id.placeholder);
        if (placeholder != null) {
            if (groupF == null)
                groupF = GroupsFragment.newInstance();
            //getSupportFragmentManager().beginTransaction().addToBackStack("hej").add(R.id.placeholder, groupF).commit();
            //getSupportFragmentManager().beginTransaction().addToBackStack("hej").add(R.id.placeholder, groupF).commit();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.placeholder, groupF);
            transaction.commit();
            getSupportFragmentManager().setFragmentResultListener("infoFromMain", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (innerF == null)
                        innerF = InnerFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.placeholder, innerF);
                    transaction.commit();
                }
            });
     }*/
}