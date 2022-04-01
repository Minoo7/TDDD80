package com.vinga129.a3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vinga129.a3.retro.RetrofitClient;
import com.vinga129.a3.retro.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private OnBackListener onBackListener;
    public InfoViewModel dataViewModel;
    public UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        service = RetrofitClient.getRetrofitInstance().create(UserService.class);
    }

    @Override
    public void onBackPressed() {
        if (onBackListener != null)
            onBackListener.onBackPress();
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    @FunctionalInterface
    public interface OnBackListener {
        void onBackPress();
    }

    public <T> void doNetworkCall(NetworkReceiver receiver, Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                receiver.onNetworkReceived(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {}
        });
    }
}