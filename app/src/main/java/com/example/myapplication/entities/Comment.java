package com.example.myapplication.entities;

import android.os.Build;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment implements Serializable {
    private String user;
    private String comment;
    private String timestamp;

    public Comment(String user, String comment) {
        this.user = user;
        this.comment = comment;
        this.timestamp = getCurrentTimestamp();
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String getCurrentTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return currentDateTime.format(formatter);
        } else {
            // Handle the case for pre-Oreo devices (SDK < 26)
            return "N/A"; // or use a different date/time method
        }
    }
}
