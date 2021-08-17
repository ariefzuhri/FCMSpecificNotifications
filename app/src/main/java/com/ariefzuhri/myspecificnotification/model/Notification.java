package com.ariefzuhri.myspecificnotification.model;

public class Notification {

    // Don't rename the columns
    final String title;
    final String body;
    final String priority;
    final String click_action;

    public Notification(String title, String body, String priority, String clickAction) {
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.click_action = clickAction;
    }
}
