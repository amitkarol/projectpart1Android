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
        int videoRawResource1 = R.raw.video1; //dior
        int videoRawResource2 = R.raw.video2; //snail ice cream
        int videoRawResource3 = R.raw.video3; //paris
        int videoRawResource4 = R.raw.policesnail;
        int videoRawResource5 = R.raw.newrules;
        int videoRawResource6 = R.raw.disney;
        int videoRawResource7 = R.raw.lionking;
        int videoRawResource8 = R.raw.basketballplayer;
        int videoRawResource9 = R.raw.london;
        int videoRawResource10 = R.raw.eras;



        // Construct the video URL using the resource identifier
        String videoUrl1 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource1;
        String videoUrl2 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource2;
        String videoUrl3 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource3;
        String videoUrl4 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource4;
        String videoUrl5 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource5;
        String videoUrl6 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource6;
        String videoUrl7 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource7;
        String videoUrl8 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource8;
        String videoUrl9 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource9;
        String videoUrl10 = "android.resource://" + "com.example.myapplication" + "/" + videoRawResource10;


        // Construct the photo URLs using the resource identifier
        String photo1 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.dior;
        String photo2 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.snailicecream;
        String photo3 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.paris;
        String photo4 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.policesnail;
        String photo5 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.newrules;
        String photo6 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.disney;
        String photo7 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.lion;
        String photo8 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.basketball;
        String photo9 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.london;
        String photo10 = "android.resource://" + "com.example.myapplication" + "/" + R.drawable.eras;




        // Create sample video data with unique IDs
        video video1 = new video(UUID.randomUUID().toString(), "Dior gallery - Paris", "So beautiful", photo1, R.drawable.dior, videoUrl1, "maayan@gmail.com", 0, 0);
        video video2 = new video(UUID.randomUUID().toString(), "Mr. Snail - Ice cream Guy", "Mr. Snail's ice cream is the BEST!", photo2, R.drawable.snailicecream, videoUrl2, "hemi@gmail.com", 0, 0);
        video video3 = new video(UUID.randomUUID().toString(), "Paris - breakfast at Carette", "Paris was delicious", photo3, R.drawable.paris, videoUrl3, "maayan@gmail.com", 0, 0);
        video video4 = new video(UUID.randomUUID().toString(), "Mr. snail - police officer", "Mr. snail is the BEST police officer", photo4, R.drawable.policesnail, videoUrl4, "hemi@gmail.com", 0, 0);
        video video5 = new video(UUID.randomUUID().toString(), "New rules - Dua Lipa", "Best Clip Ever", photo5, R.drawable.newrules, videoUrl5, "amit@gmail.com", 0, 0);
        video video6 = new video(UUID.randomUUID().toString(), "Disneyland", "Disney was magical", photo6, R.drawable.disney, videoUrl6, "amit@gmail.com", 0, 0);
        video video7 = new video(UUID.randomUUID().toString(), "Lions king Musical London", "Hakuna Matata!", photo7, R.drawable.lion, videoUrl7, "amit@gmail.com", 0, 0);
        video video8 = new video(UUID.randomUUID().toString(), "Mr. Snail basketball Player", "Mr. Snail is the BEST basketball Player", photo8, R.drawable.basketball, videoUrl8, "hemi@gmail.com", 0, 0);
        video video9 = new video(UUID.randomUUID().toString(), "Leaving London", "So sad to leave london", photo9, R.drawable.london, videoUrl9, "maayan@gmail.com", 0, 0);
        video video10 = new video(UUID.randomUUID().toString(), "You Need To Calm Down - The Eras Tour", "This night was sparkling", photo10, R.drawable.eras, videoUrl10, "maayan@gmail.com", 0, 0);


        // Add comments to videos
        video1.addComment(new Comment("Hemi", "Great video!", photo1));
        video1.addComment(new Comment("User2", "Thanks for sharing!", photo2));

        video2.addComment(new Comment("User3", "Very informative.", photo2));
        video2.addComment(new Comment("User4", "Loved it!", photo3));

        video3.addComment(new Comment("User5", "Amazing content.", photo3));
        video3.addComment(new Comment("User6", "Keep it up!", photo1));

        // Add videos to the set
        videoSet.add(video1);
        videoSet.add(video2);
        videoSet.add(video3);
        videoSet.add(video4);
        videoSet.add(video5);
        videoSet.add(video6);
        videoSet.add(video7);
        videoSet.add(video8);
        videoSet.add(video9);
        videoSet.add(video10);
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
