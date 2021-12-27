package com.ariefzuhri.myspecificnotification.data.network

import com.ariefzuhri.myspecificnotification.data.response.FcmResponse
import retrofit2.http.POST
import com.ariefzuhri.myspecificnotification.BuildConfig
import com.ariefzuhri.myspecificnotification.data.model.Message
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