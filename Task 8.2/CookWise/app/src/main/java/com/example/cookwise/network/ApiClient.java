package com.example.cookwise.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:5000"; // or use LAN IP for real device
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // 🔧 OkHttpClient with custom timeout
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.MINUTES)  // time to connect
                    .readTimeout(60, TimeUnit.MINUTES)     // time to read response
                    .writeTimeout(60, TimeUnit.MINUTES)    // time to send request
                    .build();

            // 🔧 Retrofit with Gson + OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
