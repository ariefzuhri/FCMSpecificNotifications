package com.ariefzuhri.myspecificnotification.model

// Don't rename the columns
data class Notification(
    val title: String,
    val body: String,
    val priority: String,
    val click_action: String,
)