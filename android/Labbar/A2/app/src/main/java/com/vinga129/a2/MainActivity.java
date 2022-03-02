package com.vinga129.a2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getSupportFragmentManager().popBackStack();
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