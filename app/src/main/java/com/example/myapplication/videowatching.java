package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;
import com.example.myapplication.Fragments.Comments;
import com.example.myapplication.AddCommentDialog;

public class videowatching extends FragmentActivity {

    private static final String TAG = "videowatching";

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView channelTextView;
    private TextView viewCountTextView;
    private VideoView videoView;
    private Button likeButton;
    private Button shareButton;
    private Button commentsButton;
    private Button addCommentButton;
    private Button editButton;
    private Button pauseResumeButton;
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
        addCommentButton = findViewById(R.id.addCommentButton);
        editButton = findViewById(R.id.editButton);
        pauseResumeButton = findViewById(R.id.pauseResumeButton);

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
            if (currentVideo != null && loggedInUser != null) {
                Log.d(TAG, "Editing video: " + currentVideo.getTitle() + " by user: " + loggedInUser.getDisplayName());
                Intent editIntent = new Intent(videowatching.this, EditVideoActivity.class);
                editIntent.putExtra("video", currentVideo);
                editIntent.putExtra("user", loggedInUser);
                startActivity(editIntent);
            } else {
                Log.e(TAG, "Current video or logged-in user is null");
            }
        });

        // Pause/Resume button listener
        pauseResumeButton.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                pauseResumeButton.setText("Resume");
            } else {
                videoView.start();
                pauseResumeButton.setText("Pause");
            }
        });

        // Comments button listener
        commentsButton.setOnClickListener(v -> {
            Comments commentsFragment = new Comments(currentVideo);
            commentsFragment.show(getSupportFragmentManager(), "CommentsFragment");
        });

        // Add Comment button listener
        addCommentButton.setOnClickListener(v -> {
            AddCommentDialog addCommentDialog = new AddCommentDialog(this, currentVideo, loggedInUser, newComment -> {
                // Update the UI or perform actions after the comment is added
                updateCommentsButton();
            });
            addCommentDialog.show();
        });

        // Initial update of comments button
        updateCommentsButton();
    }

    private void updateCommentsButton() {
        if (currentVideo != null) {
            int commentCount = currentVideo.getComments().size();
            commentsButton.setText("Comments (" + commentCount + ")");
        }
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
