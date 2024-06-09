package com.example.myapplication.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment implements Serializable {
    private String username;
    private String text;
    private String timestamp;
    private String photoUri;

    // Constructor
    public Comment(String username, String text, String photoUri) {
        this.username = username;
        this.text = text;
        this.photoUri = photoUri;
        this.timestamp = getCurrentTimestamp();
    }

    // Getter and setter for photoUri
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    // Other getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String getCurrentTimestamp() {
        // Implementation for getting the current timestamp
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
