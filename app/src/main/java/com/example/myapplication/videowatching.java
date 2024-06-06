package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.myapplication.Fragments.Comments;
import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;

public class videowatching extends Activity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView channelTextView;
    private TextView viewCountTextView;
    private VideoView videoView;
    private Button likeButton;
    private Button shareButton;
    private Button commentsButton;
    private Button editButton;
    private video currentVideo;
    private user loggedInUser;

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
        shareButton = findViewById(R.id.shareButton);
        commentsButton = findViewById(R.id.commentsButton);
        editButton = findViewById(R.id.editButton);

        // Get the data passed from homescreen activity
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            currentVideo = getVideoByTitle(title);
            loggedInUser = (user) intent.getSerializableExtra("user");

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

        // Like button listener
        likeButton.setOnClickListener(v -> {
            if (currentVideo != null && loggedInUser != null) {
                if (currentVideo.hasLiked(loggedInUser.getEmail())) {
                    currentVideo.unlikeVideo(loggedInUser.getEmail());
                } else {
                    currentVideo.likeVideo(loggedInUser.getEmail());
                }
                updateLikeButton();
            }
        });

        // Edit button listener
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(videowatching.this, EditVideoActivity.class);
            editIntent.putExtra("video", currentVideo);
            editIntent.putExtra("user", loggedInUser);
            startActivity(editIntent);
        });

//        // Comments button listener
//        commentsButton.setOnClickListener(v -> {
//            Log.d("videowatching", "Comments button clicked");
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.commentsContainer, Comments.newInstance());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//            findViewById(R.id.commentsContainer).setVisibility(View.VISIBLE);
//        });
    }

    // Update the text of the like button
    private void updateLikeButton() {
        if (currentVideo != null && loggedInUser != null) {
            boolean hasLiked = currentVideo.hasLiked(loggedInUser.getEmail());
            int likeCount = currentVideo.getLikeCount();
            likeButton.setText(hasLiked ? "Unlike (" + likeCount + ")" : "Like (" + likeCount + ")");
        }
    }

    private video getVideoByTitle(String title) {
        for (video video : VideoManager.getInstance().getVideoList()) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }
}