package com.ariefzuhri.myspecificnotification.data.model

// Brings the notification message and the recipient's token
// Don't rename the columns, they have fixed object key values
data class Message(
    // Notification main data
    val notification: Notification,

    // Extra data (optional)
    val data: Data,

    // Recipient token (or specific topic, input by "/topics/{topic_name}")
    val to: String,
)

// Don't rename the columns
data class Notification(
    val title: String,
    val body: String,
    val priority: String,
    val click_action: String,
)

data class Data(
    // The variable name is the key to get the extra using getData() in MyFirebaseMessagingService
    val extraMessage: String?,
)