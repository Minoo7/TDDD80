package com.vinga129.a3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private OnBackListener onBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}