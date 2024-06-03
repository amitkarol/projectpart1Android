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

public class videowatching extends Activity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView channelTextView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videowatching);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        channelTextView = findViewById(R.id.channelTextView);
        videoView = findViewById(R.id.videoView);

        // Get the data passed from homescreen activity
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String channelName = intent.getStringExtra("channelName");
            String videoUrl = intent.getStringExtra("videoUrl");

            // Set data to views
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            channelTextView.setText(channelName);
            videoView.setVideoPath(videoUrl);
            videoView.start();
        }

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
    }
}
