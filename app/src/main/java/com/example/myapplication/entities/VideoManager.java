package com.example.myapplication.entities;

import android.os.Build;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VideoManager {
    private static VideoManager instance;
    private Set<video> videoSet;

    private VideoManager() {
        videoSet = new HashSet<>();
        // Add initial sample videos
        initializeSampleVideos();
    }

    public static synchronized VideoManager getInstance() {
        if (instance == null) {
            instance = new VideoManager();
        }
        return instance;
    }

    private void initializeSampleVideos() {
        int videoRawResource1 = R.raw.video1;
        int videoRawResource2 = R.raw.video2;
        int videoRawResource3 = R.raw.video3;

        // Construct the video URL using the resource identifier
        String videoUrl1 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource1;
        String videoUrl2 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource2;
        String videoUrl3 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource3;

        // Construct the photo URLs using the resource identifier
        String photo1 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.dog1;
        String photo2 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.dog2;
        String photo3 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.dog1;

        // Create sample video data with unique IDs
        video video1 = new video(UUID.randomUUID().toString(), "Video Title 1", "Description 1", photo1, R.drawable.dog1, videoUrl1, "Channel 1", 0, 0);
        video video2 = new video(UUID.randomUUID().toString(), "Video Title 2", "Description 2", photo2, R.drawable.dog2, videoUrl2, "Channel 2", 0, 0);
        video video3 = new video(UUID.randomUUID().toString(), "Video Title 3", "Description 3", photo3, R.drawable.dog1, videoUrl3, "Channel 3", 0, 0);

        // Add comments to videos
        video1.addComment(new Comment("User1", "Great video!", photo1));
        video1.addComment(new Comment("User2", "Thanks for sharing!", photo2));

        video2.addComment(new Comment("User3", "Very informative.", photo2));
        video2.addComment(new Comment("User4", "Loved it!", photo3));

        video3.addComment(new Comment("User5", "Amazing content.", photo3));
        video3.addComment(new Comment("User6", "Keep it up!", photo1));

        // Add videos to the set
        videoSet.add(video1);
        videoSet.add(video2);
        videoSet.add(video3);
    }

    public List<video> getVideoList() {
        return new ArrayList<>(videoSet);
    }

    public video getVideoById(String id) {
        for (video video : videoSet) {
            if (video.getId().equals(id)) {
                return video;
            }
        }
        return null;
    }

    public video getVideoByTitle(String title) {
        for (video video : videoSet) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }

    public void addVideo(video newVideo) {
        videoSet.add(newVideo);
    }

    public void updateVideo(video updatedVideo , video originvideo) {
        removeVideo(originvideo);
        videoSet.add(updatedVideo);
    }

    public void removeVideo(video videoToRemove) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoSet.removeIf(video -> video.getId().equals(videoToRemove.getId())); // Ensure removal by ID
        }
    }

    public void clearVideos() {
        videoSet.clear();
        initializeSampleVideos();
    }
}
