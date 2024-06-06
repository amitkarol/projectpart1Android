package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;

import java.util.List;

import adapter.VideoListAdapter;

public class homescreen extends Activity {

    private RecyclerView recyclerView;
    private VideoListAdapter videoAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private user loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        loggedInUser = (user) getIntent().getSerializableExtra("user");
        if (loggedInUser == null) {
            loggedInUser = new user("Test", "User", "testuser@example.com", "Password@123", "TestUser", "fake_uri");
        }

        // Display user photo
        ImageView imageViewPerson = findViewById(R.id.imageViewPerson);
        if (loggedInUser.getPhotoUri() != null) {
            imageViewPerson.setImageURI(Uri.parse(loggedInUser.getPhotoUri()));
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the video list from VideoManager
        refreshVideoList();

        // Set up SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshVideoList();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Set an OnClickListener to the imageViewPerson
        imageViewPerson.setOnClickListener(v -> {
            // Start the logout activity with user details as extras
            Intent logoutIntent = new Intent(homescreen.this, logout.class);
            logoutIntent.putExtra("user", loggedInUser);
            startActivity(logoutIntent);
        });

        // Click the button to upload video and continue to the page of upload video
        Button buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(this, uploadvideo.class);
            uploadIntent.putExtra("user", loggedInUser); // Pass the user object
            startActivity(uploadIntent);
        });

        // Set up SearchView
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Perform the final search
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Filter the list as the user types
//                videoAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
    }

    private void refreshVideoList() {
        List<video> videoList = VideoManager.getInstance().getVideoList();
        videoAdapter = new VideoListAdapter(videoList, this, loggedInUser);
        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshVideoList();
    }
}
