package com.ariefzuhri.myspecificnotification.network

import com.ariefzuhri.myspecificnotification.response.FcmResponse
import retrofit2.http.POST
import com.ariefzuhri.myspecificnotification.BuildConfig
import com.ariefzuhri.myspecificnotification.model.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

interface ApiService {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key= ${BuildConfig.FIREBASE_SERVER_KEY}"
    )

    @POST("fcm/send")
    fun sendNotification(@Body body: Message): Call<FcmResponse>
}