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
            ***REMOVED***
    )

    @POST("fcm/send")
    // Fungsi ini dipanggil ketika akan mengirim notifikasi (isi notifikasi dan token dibungkus dalam kelas Sender)
    Call<MyResponse> sendNotification(@Body Sender body);
***REMOVED***