package com.example.myapplication.entities;

import java.util.ArrayList;
import java.util.List;

public class video {
        private String title;
        private String description;
        private int thumbnailUrl; // URL of the thumbnail image representing the video
        private String videoUrl; // URL of the actual video

    public String getDescription() {
        return description;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    private String channelName;
        private int viewCount;
        private int likeCount;
        private List<Comment> comments; // List of comments for the video

        // Constructor
        public video(String title, String description, int thumbnailUrl, String videoUrl, String channelName, int viewCount, int likeCount) {
            this.title = title;
            this.description = description;
            this.thumbnailUrl = thumbnailUrl;
            this.videoUrl = videoUrl;
            this.channelName = channelName;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.comments = new ArrayList<>();
        }

    // Getters and Setters for existing fields

    public String getChannelName() {
        return channelName;
    }

    public int getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    // Getters and Setters for comments
        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        // Method to add a comment
        public void addComment(Comment comment) {
            comments.add(comment);
        }

        // toString method for easy debugging and logging
        @Override
        public String toString() {
            return "YouTubeVideo{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", thumbnailUrl='" + thumbnailUrl + '\'' +
                    ", videoUrl='" + videoUrl + '\'' +
                    ", channelName='" + channelName + '\'' +
                    ", viewCount=" + viewCount +
                    ", likeCount=" + likeCount +
                    ", comments=" + comments +
                    '}';
        }
    }

