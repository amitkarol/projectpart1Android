package com.example.myapplication.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class video implements Serializable {
    private String title;
    private String description;
    private String thumbnailUrl; // For external images
    private int thumbnailResId; // For drawable resource IDs
    private String videoUrl;
    private String channelName;
    private int viewCount;
    private int likeCount;
    private Map<String, Boolean> userLikes; // Track likes per user

    public video(String title, String description, String thumbnailUrl, int thumbnailResId, String videoUrl, String channelName, int viewCount, int likeCount) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailResId = thumbnailResId;
        this.videoUrl = videoUrl;
        this.channelName = channelName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.userLikes = new HashMap<>();
    }

    public video(String videoUrl) {
        this.videoUrl = videoUrl;
        this.userLikes = new HashMap<>();
    }


    // Copy constructor
    public video(video original) {
        this.title = original.title;
        this.description = original.description;
        this.thumbnailUrl = original.thumbnailUrl;
        this.thumbnailResId = original.thumbnailResId;
        this.videoUrl = original.videoUrl;
        this.channelName = original.channelName;
        this.likeCount = original.likeCount;
        this.viewCount = original.viewCount;
    }

    // Getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }

    public void setThumbnailResId(int thumbnailResId) {
        this.thumbnailResId = thumbnailResId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Map<String, Boolean> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Map<String, Boolean> userLikes) {
        this.userLikes = userLikes;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void likeVideo(String username) {
        if (!userLikes.containsKey(username) || !userLikes.get(username)) {
            userLikes.put(username, true);
            this.likeCount++;
        }
    }

    public void unlikeVideo(String username) {
        if (userLikes.containsKey(username) && userLikes.get(username)) {
            userLikes.put(username, false);
            this.likeCount--;
        }
    }

    public boolean hasLiked(String username) {
        return userLikes.containsKey(username) && userLikes.get(username);
    }

    @Override
    public String toString() {
        return "video{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thumbnailResId=" + thumbnailResId +
                ", videoUrl='" + videoUrl + '\'' +
                ", channelName='" + channelName + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", userLikes=" + userLikes +
                '}';
    }
}
