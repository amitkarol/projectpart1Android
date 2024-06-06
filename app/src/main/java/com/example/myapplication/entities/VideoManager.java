package com.example.myapplication.entities;

import android.os.Build;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Create sample video data
        videoSet.add(new video("Video Title 1", "Description 1", photo1, R.drawable.dog1, videoUrl1, "Channel 1", 0, 0));
        videoSet.add(new video("Video Title 2", "Description 2", photo2, R.drawable.dog2, videoUrl2, "Channel 2", 0, 0));
        videoSet.add(new video("Video Title 3", "Description 3", photo3, R.drawable.dog1, videoUrl3, "Channel 3", 0, 0));
    }

    public List<video> getVideoList() {
        return new ArrayList<>(videoSet);
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
            videoSet.removeIf(video -> video.getVideoUrl().equals(videoToRemove.getVideoUrl())); // Ensure removal by URL
        }
    }
    public void clearVideos() {
        videoSet.clear();
        initializeSampleVideos();
    }
}
