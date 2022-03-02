package com.vinga129.lec2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MainFragment mf;
    InfoFragment info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View placeHolder = findViewById(R.id.placeholder);
        //System.out.println(placeHolder);
        //Log.v("Test", placeHolder.toString());
        if (placeHolder != null) { // if in portrait
            if (mf == null)
                mf = MainFragment.newInstance();

            getSupportFragmentManager().beginTransaction().add(R.id.placeholder, mf).commit();
            getSupportFragmentManager().setFragmentResultListener("infoFromMain", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    //String data = result.getString("data");
                    Log.v("Data from Fragment", "I got data from fragment");
                    if (info == null)
                        info = InfoFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.placeholder, info).commit();
                }
            });

            //else {
            //    System.out.println("yoooo");
            //}
            findViewById(R.id.switchAct2Btn).setOnClickListener((View) -> {
                // start new activity
                doSwap();
            });
        }
    }

    private void doSwap() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}