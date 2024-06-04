package com.example.myapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.myapplication.Fragments.Comments;
import com.example.myapplication.entities.video;

import java.util.ArrayList;
import java.util.List;

public class videowatching extends Activity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView channelTextView;
    private TextView viewCountTextView;
    private VideoView videoView;
    private Button likeButton;
    private video currentVideo;
    private static List<video> videoList = new ArrayList<>();

    public static List<video> getVideoList() {
        return videoList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videowatching);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        channelTextView = findViewById(R.id.channelTextView);
        viewCountTextView = findViewById(R.id.viewCountTextView);
        videoView = findViewById(R.id.videoView);
        likeButton = findViewById(R.id.likeButton);

        // Get the data passed from homescreen activity
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            currentVideo = getVideoByTitle(title);

            if (currentVideo != null) {
                // Set data to views
                titleTextView.setText(currentVideo.getTitle());
                descriptionTextView.setText(currentVideo.getDescription());
                channelTextView.setText(currentVideo.getChannelName());
                viewCountTextView.setText("Views " + currentVideo.getViewCount());
                videoView.setVideoPath(currentVideo.getVideoUrl());
                videoView.start();

                // Update view count
                currentVideo.incrementViewCount();
                viewCountTextView.setText("Views " + currentVideo.getViewCount());
                updateLikeButton();
            }
        }

        // Comments button listener
        Button commentsButton = findViewById(R.id.commentsButton);
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CommentsFragment
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.commentsContainer, Comments.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

                // Make the FrameLayout visible
                findViewById(R.id.commentsContainer).setVisibility(View.VISIBLE);
            }
        });

        // Like button listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentVideo != null) {
                    if (currentVideo.hasLiked()) {
                        currentVideo.decrementLikeCount();
                        currentVideo.setLiked(false);
                    } else {
                        currentVideo.incrementLikeCount();
                        currentVideo.setLiked(true);
                    }
                    updateLikeButton();
                }
            }
        });
    }

    // Update the text of the like button
    private void updateLikeButton() {
        if (currentVideo != null) {
            likeButton.setText(currentVideo.hasLiked() ? "Unlike (" + currentVideo.getLikeCount() + ")" : "Like (" + currentVideo.getLikeCount() + ")");
        }
    }

    private video getVideoByTitle(String title) {
        for (video video : videoList) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }
}
