package com.vinga129.savolax.ui.retrofit;

import android.content.Context;
import androidx.annotation.NonNull;
import com.vinga129.savolax.NetworkReceiver;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {

    private static Controller instance = null;
    private static Builder baseClient;
    private static Retrofit.Builder baseRetrofit;
    private static NoAuthAPI noAuthAPI;
    private static RestAPI restAPI;
    // private static final String BASE_URL = "https://tddd80-2022.herokuapp.com/";
    //public static final String BASE_URL = "http://10.0.0.7:5080/";
    public static final String BASE_URL = "http://192.168.10.172:5080/";

    private Controller() {
    }

    public void init(Context context) {
        TokenInterceptor interceptor = new TokenInterceptor(context);
        restAPI = baseRetrofit
                .client(baseClient.addInterceptor(interceptor).addNetworkInterceptor(interceptor).build()).build()
                .create(RestAPI.class);
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            baseClient = new Builder().addInterceptor(logging);
            baseRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            noAuthAPI = baseRetrofit.client(baseClient.build()).build().create(NoAuthAPI.class);
        }
        return instance;
    }

    public NoAuthAPI getNoAuthAPI() {
        return noAuthAPI;
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
            String JWT_KEY = context.getSharedPreferences
                    ("API", Context.MODE_PRIVATE).getString("JWT_KEY", "Default");
            Request newRequest = chain.request().newBuilder().header("Authorization", "Bearer " + JWT_KEY).build();

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
