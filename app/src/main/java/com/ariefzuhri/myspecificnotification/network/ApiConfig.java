package com.ariefzuhri.myspecificnotification.network;

import static com.ariefzuhri.myspecificnotification.BuildConfig.FCM_BASE_URL;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chuckerteam.chucker.api.ChuckerInterceptor;

public class ApiConfig {

    @NonNull
    public static ApiService getApiService(Context context) {
        ChuckerInterceptor loggingInterceptor = new ChuckerInterceptor.Builder(context)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
