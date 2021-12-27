package com.ariefzuhri.myspecificnotification.data.response

// This class handles the response whether the notification was sent successfully or failed
data class FcmResponse(
    var success: Int? = null,
)