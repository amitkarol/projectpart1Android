package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entities.video;

import java.util.ArrayList;
import java.util.List;

import adapter.VideoListAdapter;

public class homescreen extends Activity {

    private RecyclerView recyclerView;
    private VideoListAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the resource identifier of your video file
        int videoRawResource1 = R.raw.video1;
        int videoRawResource2 = R.raw.video2;
        int videoRawResource3 = R.raw.video3;

        // Construct the video URL using the resource identifier
        String videoUrl = "android.resource://" + getPackageName() + "/" + videoRawResource1;
        String videoUrl2 = "android.resource://" + getPackageName() + "/" + videoRawResource2;
        String videoUrl3 = "android.resource://" + getPackageName() + "/" + videoRawResource3;
        // Create sample video data
        List<video> videoList = new ArrayList<>();
        videoList.add(new video("Video Title 1", "Channel Name 1", R.drawable.dog1, videoUrl, "chnnel 1", 0, 0));
        videoList.add(new video("Video Title 2", "Channel Name 2", R.drawable.dog2, videoUrl2, "channel2", 0, 0));
        videoList.add(new video("Video Title 3", "Channel Name 3", R.drawable.dog2, videoUrl3, "channel3", 0, 0));


        // Initialize and set adapter
        videoAdapter = new VideoListAdapter(videoList, this);
        recyclerView.setAdapter(videoAdapter);

        Intent intent = getIntent();
        String photoUri = intent.getStringExtra("photoUri");

        ImageView imageViewPerson = findViewById(R.id.imageViewPerson);
        if (photoUri != null) {
            // Load the photo from URI
            imageViewPerson.setImageURI(Uri.parse(photoUri));
        }


        // Set an OnClickListener to the buttonPerson button
        imageViewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user details from the Intent
                Intent intent = getIntent();
                String firstName = intent.getStringExtra("firstName");
                String lastName = intent.getStringExtra("lastName");
                String username = intent.getStringExtra("username");
                String displayName = intent.getStringExtra("displayName");
                String photoUri = intent.getStringExtra("photoUri");

                // Start the logout activity with user details as extras
                Intent logoutIntent = new Intent(homescreen.this, logout.class);
                logoutIntent.putExtra("firstName", firstName);
                logoutIntent.putExtra("lastName", lastName);
                logoutIntent.putExtra("username", username);
                logoutIntent.putExtra("displayName", displayName);
                logoutIntent.putExtra("photoUri", photoUri); // Pass the photo URI to the logout activity
                startActivity(logoutIntent);
            }
        });

        Button buttonPlus = findViewById(R.id.buttonUpload);

        buttonPlus.setOnClickListener( v-> {
            Intent intent2 = new Intent(this, uploadvideo.class);
            startActivity(intent2);
        });
    }
}
