package com.ariefzuhri.myspecificnotification.model;

// Brings the notification and the recipient's token
public class Sender {

    // Don't rename the columns to parse as JSON
    public final Notification data;
    public final String to; // Recipient tokens

    public Sender(Notification data, String to) {
        this.data = data;
        this.to = to;
    ***REMOVED***
***REMOVED***
