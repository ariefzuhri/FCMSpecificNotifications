package com.ariefzuhri.myspecificnotification.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.ariefzuhri.myspecificnotification.response.FcmResponse
import com.ariefzuhri.myspecificnotification.network.ApiConfig
import com.google.firebase.database.DatabaseError
import android.content.Context
import android.util.Log
import com.ariefzuhri.myspecificnotification.model.Data
import com.ariefzuhri.myspecificnotification.model.Message
import com.ariefzuhri.myspecificnotification.model.Notification
import com.ariefzuhri.myspecificnotification.model.Token
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * - Get the specific token if you want to send push notifications to a specific user
 * - Subscribe to related topics from your device if you want to get push notifications with a specific topic */

private const val TAG = "FcmUtils"

// Call this every launch the app to get and store the token
fun registerToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
        if (task.isSuccessful) {
            val token = task.result
            sendRegistrationToServer(token)
        } else {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
        }
    }
}

fun sendNotification(
    context: Context,
    recipientId: String, notificationTitle: String, notificationMessage: String, data: Data,
) {
    Log.d(TAG, "sendNotification called")
    val reference = FirebaseDatabase.getInstance().getReference("token")
    // Get recipient token based on user id
    val query = reference.orderByKey().equalTo(recipientId)
    query.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d(
                TAG,
                "sendNotification onDataChange: success get recipient token (${dataSnapshot.children})"
            )
            dataSnapshot.children.forEach { snapshot ->
                val token = snapshot.getValue(Token::class.java)
                val notification = Notification(
                    notificationTitle, notificationMessage,
                    "high", INTENT_ACTION_DETAILS
                )
                val message = Message(notification, data, token?.token.orEmpty())

                val client = ApiConfig.getApiService(context).sendNotification(message)
                client.enqueue(object : Callback<FcmResponse?> {
                    override fun onResponse(
                        call: Call<FcmResponse?>,
                        response: Response<FcmResponse?>,
                    ) {
                        if (response.code() == 200 && response.body() != null) {
                            if (response.body()!!.success == 1) {
                                Log.d(TAG, "sendNotification onResponse: notification sent")
                            }
                        } else {
                            Log.e(
                                TAG,
                                "sendNotification onResponse: error code ${response.code()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<FcmResponse?>, t: Throwable) {
                        Log.e(TAG, "sendNotification onFailure", t)
                    }
                })
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e(TAG, "sendNotification onCancelled", error.toException())
        }
    })
}

// Store latest token to database server
fun sendRegistrationToServer(newToken: String) {
    Log.d(TAG, "sendRegistrationToServer called: $newToken")
    // This token is required as a notification receiver
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    if (firebaseUser != null) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("token")
        val token = Token(newToken)
        databaseReference.child(firebaseUser.uid)
            .setValue(token)
            .addOnSuccessListener {
                Log.d(TAG, "sendRegistrationToServer onSuccess")
            }
            .addOnFailureListener {
                Log.e(TAG, "sendRegistrationToServer onFailure", it)
            }
    } else Log.w(TAG, "sendRegistrationToServer cancelled: no user logged in")
}

// It's fine if you call this every launch the app
fun subscribeToTopic(topic: String) {
    FirebaseMessaging.getInstance().subscribeToTopic(topic)
        .addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) Log.d(TAG, "Success subscribe to $topic")
            else Log.w(TAG, "Failed to subscribe to $topic", task.exception)
        }
}

fun unsubscribeFromTopic(topic: String) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        .addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) Log.d(TAG, "Success unsubscribe from $topic")
            else Log.w(TAG, "Failed to unsubscribe from $topic", task.exception)
        }
}