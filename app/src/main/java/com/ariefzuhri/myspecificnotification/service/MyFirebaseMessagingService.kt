package com.ariefzuhri.myspecificnotification.service

import com.ariefzuhri.myspecificnotification.R
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.app.NotificationManager
import android.os.Build
import android.app.NotificationChannel
import android.util.Log
import com.ariefzuhri.myspecificnotification.utils.EXTRA_MESSAGE
import com.ariefzuhri.myspecificnotification.utils.TAG
import com.ariefzuhri.myspecificnotification.utils.sendRegistrationToServer
import java.util.*

/**
 * Read the documentation here: https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages
 * FCM HTTP protocol: https://firebase.google.com/docs/cloud-messaging/http-server-ref
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * This class is a service class that runs in the background,
     * detecting every time a new notification is received
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // If you don't receive a notification, please check the topic subscription
        super.onMessageReceived(remoteMessage)
        showNotification(remoteMessage)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val channelId = getString(R.string.fcm_default_notification_channel_id)
        val channelName = getString(R.string.fcm_default_notification_channel_name)

        // Get messages from getNotification()
        val title = remoteMessage.notification?.title.orEmpty()
        val message = remoteMessage.notification?.body.orEmpty()
        val clickAction = remoteMessage.notification?.clickAction.orEmpty()

        // Get extras from getData()
        val extraMessage = remoteMessage.data["extraMessage"]

        // Don't forget to add intent filter for intent action in your manifest
        val intent = Intent(clickAction)
        intent.putExtra(EXTRA_MESSAGE, extraMessage)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        @SuppressLint("UnspecifiedImmutableFlag") val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(ContextCompat.getColor(this, android.R.color.transparent))
            .setSound(defaultSoundUri)
            .setAutoCancel(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, remoteMessage.priority
            )
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = builder.build()
        // Keep multiple notifications
        val notificationId = Random().nextInt()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        sendRegistrationToServer(newToken)
        Log.d(TAG, "Refreshed token: $newToken")
    }
}