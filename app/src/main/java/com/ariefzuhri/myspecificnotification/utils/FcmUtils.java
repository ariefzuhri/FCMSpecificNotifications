package com.ariefzuhri.myspecificnotification.utils;

import static com.ariefzuhri.myspecificnotification.utils.Constants.INTENT_ACTION_DETAILS;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ariefzuhri.myspecificnotification.model.Data;
import com.ariefzuhri.myspecificnotification.model.Message;
import com.ariefzuhri.myspecificnotification.model.Notification;
import com.ariefzuhri.myspecificnotification.model.Token;
import com.ariefzuhri.myspecificnotification.network.ApiConfig;
import com.ariefzuhri.myspecificnotification.response.FcmResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* - Get the specific token if you want to send push notifications to a specific user
 * - Subscribe to related topics from your device if you want to get push notifications with a specific topic */
public class FcmUtils {

    private static final String TAG = FcmUtils.class.getSimpleName();

    // Call this every launch the app to get and store the token
    public static void registerToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                sendRegistrationToServer(token);
            } else {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            }
        });
    }

    public static void sendNotification(Context context, String recipientId, String title, String message, Data data) {
        Log.d(TAG, "sendNotification called");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("token");
        // Get recipient token based on user id
        Query query = reference.orderByKey().equalTo(recipientId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "sendNotification onDataChange: success get recipient token (" + dataSnapshot.getChildrenCount() + ")");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);

                    Log.d(TAG, "sendNotification: is token not null = " + (token != null));
                    if (token != null) {
                        Notification notification = new Notification(title, message, "high", INTENT_ACTION_DETAILS);
                        Message message = new Message(notification, data, token.getToken());

                        Call<FcmResponse> client = ApiConfig.getApiService(context).sendNotification(message);
                        client.enqueue(new Callback<FcmResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<FcmResponse> call, @NonNull Response<FcmResponse> response) {
                                if (response.code() == 200 && response.body() != null) {
                                    if (response.body().success == 1) {
                                        Log.d(TAG, "sendNotification onResponse: notification sent");
                                    }
                                } else {
                                    Log.e(TAG, "sendNotification onResponse: error code " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<FcmResponse> call, @NonNull Throwable t) {
                                Log.e(TAG, "sendNotification onFailure", t);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "sendNotification onCancelled", error.toException());
            }
        });
    }

    // Store latest token to database server
    public static void sendRegistrationToServer(String newToken) {
        Log.d(TAG, "sendRegistrationToServer called: " + newToken);
        // This token is required as a notification receiver
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("token");
            Token token = new Token(newToken);
            databaseReference.child(firebaseUser.getUid())
                    .setValue(token)
                    .addOnSuccessListener(unused -> Log.d(TAG, "sendRegistrationToServer onSuccess"))
                    .addOnFailureListener(e -> Log.e(TAG, "sendRegistrationToServer onFailure", e));
        } else Log.w(TAG, "sendRegistrationToServer cancelled: no user logged in");
    }

    // It's fine if you call this every launch the app
    public static void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(task -> {
            if (task.isSuccessful()) Log.d(TAG, "Success subscribe to " + topic);
            else Log.w(TAG, "Failed to subscribe to " + topic, task.getException());
        });
    }

    public static void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(task -> {
            if (task.isSuccessful()) Log.d(TAG, "Success unsubscribe from " + topic);
            else Log.w(TAG, "Failed to unsubscribe from " + topic, task.getException());
        });
    }
}
