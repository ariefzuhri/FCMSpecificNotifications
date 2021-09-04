package com.ariefzuhri.myspecificnotification.model

// Brings the notification message and the recipient's token
// Don't rename the columns, they have fixed object key values
data class Message(
    // Extra data (optional)
    val notification: Notification,

    // Recipient token (or specific topic "/topics/{topic_name}")
    val data: Data,

    val to: String,
)