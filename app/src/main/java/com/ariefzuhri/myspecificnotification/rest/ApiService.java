package com.ariefzuhri.myspecificnotification.rest;

import com.ariefzuhri.myspecificnotification.model.Sender;
import com.ariefzuhri.myspecificnotification.response.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.ariefzuhri.myspecificnotification.BuildConfig.SERVER_KEY;

public interface ApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" + SERVER_KEY
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}