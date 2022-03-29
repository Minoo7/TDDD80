package com.vinga129.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView output = findViewById(R.id.output);
        ((Button) findViewById(R.id.button)).setOnClickListener((View) -> {
            UserService service = RetrofitClient.getRetrofitInstance().create(UserService.class);
            Call<RetroUser> user_call = service.getUser();
            user_call.enqueue(new Callback<RetroUser>() {
                @Override
                public void onResponse(@NonNull Call<RetroUser> call, @NonNull Response<RetroUser> response) {
                    RetroUser user = response.body();
                    if (user != null)
                        output.setText(user.getJob().getEmployer());
                }

                @Override
                public void onFailure(@NonNull Call<RetroUser> call, @NonNull Throwable t) {
                    Log.i("Retrodata 2",t.getMessage());
                }
            });
        });
    }
}