package com.ariefzuhri.myspecificnotification.model;

// Brings the notification message and the recipient's token
public class Message {

    // Don't rename the columns, they have fixed object key values
    final Notification notification;
    final Data data; // Extra data (optional)
    final String to; // Recipient token (or specific topic "/topics/{topic_name}")

    public Message(Notification notification, Data data, String to) {
        this.notification = notification;
        this.data = data;
        this.to = to;
    }
}
