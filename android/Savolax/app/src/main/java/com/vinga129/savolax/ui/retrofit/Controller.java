package com.vinga129.savolax.ui.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://127.0.0.1:5080/";
    //private static final String BASE_URL = "http://ip.jsontest.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
