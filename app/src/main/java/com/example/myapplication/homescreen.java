package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;

import java.util.List;

import adapter.VideoListAdapter;

public class homescreen extends Activity {

    private static final String TAG = "homescreenActivity";
    private RecyclerView recyclerView;
    private VideoListAdapter videoAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private user loggedInUser;
    private Switch modeSwitch;
    private RelativeLayout homeScreenLayout;
    private ThemeChangeReceiver themeChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        Log.d(TAG, "onCreate: Applying theme");

        // Initialize the receiver
        themeChangeReceiver = new ThemeChangeReceiver();

        // Register the theme change receiver
        IntentFilter filter = new IntentFilter(ThemeUtil.THEME_CHANGED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(themeChangeReceiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(themeChangeReceiver, filter);
        }

        loggedInUser = (user) getIntent().getSerializableExtra("user");
        if (loggedInUser == null) {
            loggedInUser = new user("Test", "User", "testuser@example.com", "Password@123", "TestUser", "fake_uri");
            Log.w(TAG, "onCreate: No user data received, using default user");
        } else {
            Log.d(TAG, "onCreate: Received user data: " + loggedInUser);
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
            Log.d(TAG, "SwipeRefreshLayout: Refresh triggered");
            refreshVideoList();
            swipeRefreshLayout.setRefreshing(false);
            Log.d(TAG, "SwipeRefreshLayout: Refresh completed");
        });

        // Set an OnClickListener to the imageViewPerson
        imageViewPerson.setOnClickListener(v -> {
            if ("testuser@example.com".equals(loggedInUser.getEmail())) {
                // Redirect to login screen if the test user is connected
                Intent loginIntent = new Intent(homescreen.this, login.class);
                startActivity(loginIntent);
            } else {
                // Start the logout activity with user details as extras
                Intent logoutIntent = new Intent(homescreen.this, logout.class);
                logoutIntent.putExtra("user", loggedInUser);
                startActivity(logoutIntent);
            }
        });

        // Click the button to upload video and continue to the page of upload video
        Button buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(this, uploadvideo.class);
            uploadIntent.putExtra("user", loggedInUser); // Pass the user object
            startActivity(uploadIntent);
        });

        // Initialize UI elements for manual theme change
        homeScreenLayout = findViewById(R.id.homeScreenLayout);

        // Read saved preferences for theme
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean("night_mode", false);
        applyTheme(isNightMode);

        modeSwitch = findViewById(R.id.switch1);
        modeSwitch.setChecked(isNightMode); // Set switch state based on saved preference

        modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "onCheckedChanged: Theme switch toggled, isChecked: " + isChecked);
            // Save the new theme preference
            SharedPreferences.Editor editor = getSharedPreferences("theme_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("night_mode", isChecked);
            editor.apply();

            // Apply the theme dynamically
            applyTheme(isChecked);
        });

        Log.d(TAG, "onCreate: Activity created successfully");
    }

    private void refreshVideoList() {
        Log.d(TAG, "refreshVideoList: Refreshing video list");
        List<video> videoList = VideoManager.getInstance().getVideoList();
        videoAdapter = new VideoListAdapter(videoList, this, loggedInUser);
        recyclerView.setAdapter(videoAdapter);
        Log.d(TAG, "refreshVideoList: Video list refreshed and adapter set");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Refreshing video list");
        refreshVideoList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(themeChangeReceiver);
        Log.d(TAG, "onDestroy: Unregistering theme change receiver");
    }

    private void applyTheme(boolean isNightMode) {
        int backgroundColor;
        int textColor;

        if (isNightMode) {
            backgroundColor = getResources().getColor(R.color.darkBackground);
            textColor = getResources().getColor(R.color.white);
        } else {
            backgroundColor = getResources().getColor(R.color.white);
            textColor = getResources().getColor(R.color.black);
        }

        homeScreenLayout.setBackgroundColor(backgroundColor);
        changeTextColor(homeScreenLayout, textColor);
        changeTextColor(recyclerView, textColor);  // Apply to RecyclerView items
        Log.d(TAG, "applyTheme: Applying theme, isNightMode: " + isNightMode);
    }

    private void changeTextColor(View view, int color) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                changeTextColor(child, color);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof Button) {
            ((Button) view).setTextColor(color);
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(color);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: Configuration changed, applying theme");
        // Apply theme based on the current night mode setting
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean("night_mode", false);
        applyTheme(isNightMode);
    }
}
