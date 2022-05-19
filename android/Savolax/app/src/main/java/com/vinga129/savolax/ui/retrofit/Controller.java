package com.vinga129.savolax.ui.retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.vinga129.savolax.NetworkReceiver;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    private static Controller INSTANCE = null;
    private RestAPI restAPI;
    private static RestAPI unauthedService;
    // private static final String BASE_URL = "https://tddd80-2022.herokuapp.com/";
    public static final String BASE_URL = "http://10.0.0.7:5080/";

    private Controller() {
    }

    public void init(Context context) {
        TokenInterceptor interceptor = new TokenInterceptor(context);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .build();
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        restAPI = retrofit.create(RestAPI.class);
    }

    public static Controller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }

    public static RestAPI getUnauthedService() {
        if (unauthedService == null) {
            unauthedService = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
                    .create(RestAPI.class);
        }
        return unauthedService;
    }

    public RestAPI getRestAPI() {
        return restAPI;
    }

    public static class TokenInterceptor implements Interceptor {
        private final Context context;

        public TokenInterceptor(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer " + context.getSharedPreferences
                            ("API", Context.MODE_PRIVATE).getString("JWT_KEY", "Default"))
                    .build();

            return chain.proceed(newRequest);
        }
    }

    public <T> void doNetwork(Call<T> call, Callback<T> callBack) {
        call.enqueue(callBack);
    }

    public <T> void doNetworkCall(NetworkReceiver receiver, Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                receiver.onNetworkReceived(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            }
        });
    }
}
