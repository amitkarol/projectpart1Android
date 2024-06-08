package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;
import com.example.myapplication.Fragments.Comments;
import com.example.myapplication.Fragments.AddCommentDialog;

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
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videowatching);

        // Initialize GestureDetector
        gestureDetector = new GestureDetectorCompat(this, new GestureListener());

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

        // Apply theme to all relevant views
        applyThemeToViews();

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
            if (currentVideo != null && !loggedInUser.getEmail().equals("testuser@example.com")) {
                Intent editIntent = new Intent(videowatching.this, EditVideoActivity.class);
                editIntent.putExtra("video", currentVideo);
                editIntent.putExtra("user", loggedInUser);
                startActivity(editIntent);
            } else {
                Intent login = new Intent(videowatching.this, login.class);
                startActivity(login);
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
            if (loggedInUser.getEmail().equals("testuser@example.com")) {
                Intent login = new Intent(videowatching.this, login.class);
                startActivity(login);
            } else {
                AddCommentDialog addCommentDialog = new AddCommentDialog(this, currentVideo, loggedInUser, newComment -> {
                    // Update the UI or perform actions after the comment is added
                    updateCommentsButton();
                });
                addCommentDialog.show();
            }
        });
        // Initial update of comments button
        updateCommentsButton();
    }

    private void applyThemeToViews() {
        int textColor = ThemeUtil.isNightMode(this) ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black);

        titleTextView.setTextColor(textColor);
        descriptionTextView.setTextColor(textColor);
        channelTextView.setTextColor(textColor);
        viewCountTextView.setTextColor(textColor);
        likeButton.setTextColor(textColor);
        shareButton.setTextColor(textColor);
        commentsButton.setTextColor(textColor);
        addCommentButton.setTextColor(textColor);
        editButton.setTextColor(textColor);
        pauseResumeButton.setTextColor(textColor);
    }

    private void updateCommentsButton() {
        if (currentVideo != null) {
            int commentCount = currentVideo.getComments().size();
            commentsButton.setText("Comments (" + commentCount + ")");
        }
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeDown();
                }
                return true;
            }
            return false;
        }
    }

    private void onSwipeDown() {
        // Go back to the previous activity
        finish();
    }
}
