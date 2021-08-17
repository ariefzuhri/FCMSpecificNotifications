package com.ariefzuhri.myspecificnotification.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ariefzuhri.myspecificnotification.R;
import com.ariefzuhri.myspecificnotification.utils.FcmUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static com.ariefzuhri.myspecificnotification.utils.Constants.EXTRA_MESSAGE;

/* Read the documentation here: https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages
 * FCM HTTP protocol: https://firebase.google.com/docs/cloud-messaging/http-server-ref */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    /* This class is a service class that runs in the background,
     * detecting every time a new notification is received */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // If you don't receive a notification, please check the topic subscription
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);
    }

    private void showNotification(@NonNull RemoteMessage remoteMessage) {
        String channelId = getString(R.string.fcm_default_notification_channel_id);
        String channelName = getString(R.string.fcm_default_notification_channel_name);

        String title = "";
        String message = "";
        String clickAction = "";

        // Get messages from getNotification()
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            clickAction = remoteMessage.getNotification().getClickAction();
        }

        // Get extras from getData()
        String extraMessage = remoteMessage.getData().get("extraMessage");

        // Don't forget to add intent filter for intent action in your manifest
        Intent intent = new Intent(clickAction);
        intent.putExtra(EXTRA_MESSAGE, extraMessage);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setSound(defaultSoundUri)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    channelName, remoteMessage.getPriority());
            builder.setChannelId(channelId);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = builder.build();
        if (notificationManager != null) {
            int notificationId = new Random().nextInt(); // Keep multiple notifications
            notificationManager.notify(notificationId, notification);
        }
    }

    /* There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data */
    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        Log.d(TAG, "Refreshed token: " + newToken);
        FcmUtils.sendRegistrationToServer(newToken);
    }
}
