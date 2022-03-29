package com.vinga129.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private OnBackListener onBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.hejsan);
        setContentView(R.layout.activity_main);
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