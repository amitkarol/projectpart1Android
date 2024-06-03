package com.example.myapplication.entities;

public class Comment {

    private user user;
    private String text;

    public Comment(user user, String text) {
        this.user = user;
        this.text = text;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "Comment{" +
                "user=" + user +
                ", text='" + text + '\'' +
                '}';
    }
}

