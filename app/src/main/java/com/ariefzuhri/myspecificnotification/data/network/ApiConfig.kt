package com.ariefzuhri.myspecificnotification.data.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.ariefzuhri.myspecificnotification.BuildConfig

object ApiConfig {

    fun getApiService(context: Context): ApiService {
        val loggingInterceptor = ChuckerInterceptor.Builder(context)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.FCM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}