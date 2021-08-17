package com.ariefzuhri.myspecificnotification.network;

import static com.ariefzuhri.myspecificnotification.BuildConfig.FIREBASE_SERVER_KEY;

import com.ariefzuhri.myspecificnotification.model.Message;
import com.ariefzuhri.myspecificnotification.response.FcmResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" + FIREBASE_SERVER_KEY
            }
    )

    @POST("fcm/send")
    Call<FcmResponse> sendNotification(@Body Message body);
}