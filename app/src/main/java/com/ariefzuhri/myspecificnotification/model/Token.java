package com.ariefzuhri.myspecificnotification.model;

public class Token {

    private String token;

    @SuppressWarnings("unused")
    public Token() {
        // Required empty constructor for Firebase
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
