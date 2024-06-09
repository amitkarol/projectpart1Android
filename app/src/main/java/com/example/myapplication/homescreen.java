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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

    private RecyclerView recyclerView;
    private VideoListAdapter videoAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private user loggedInUser;
    private Switch modeSwitch;
    private RelativeLayout homeScreenLayout;
    private ThemeChangeReceiver themeChangeReceiver;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

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
            Uri person = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.person);
            loggedInUser = new user("Test", "User", "testuser@example.com", "Password@123", "TestUser", person.toString()
            );
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
            // Save the new theme preference
            SharedPreferences.Editor editor = getSharedPreferences("theme_prefs", MODE_PRIVATE).edit();
            editor.putBoolean("night_mode", isChecked);
            editor.apply();

            // Apply the theme dynamically
            applyTheme(isChecked);
        });

        // Initialize SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideos(newText);
                return true;
            }
        });
    }

    private void refreshVideoList() {
        List<video> videoList = VideoManager.getInstance().getVideoList();
        videoAdapter = new VideoListAdapter(videoList, this, loggedInUser);
        recyclerView.setAdapter(videoAdapter);
    }

    private void filterVideos(String query) {
        if (videoAdapter != null) {
            videoAdapter.filter(query);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshVideoList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(themeChangeReceiver);
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
        recyclerView.setBackgroundColor(backgroundColor);
        changeTextColor(homeScreenLayout, textColor);
        ThemeUtil.applyThemeToRecyclerView(recyclerView, isNightMode);
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
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(color);
        } else if (view instanceof SearchView) {
            ((SearchView) view).setBackgroundColor(color);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Apply theme based on the current night mode setting
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean("night_mode", false);
        applyTheme(isNightMode);
    }

    @Override
    public void onBackPressed() {
        // Do nothing to prevent the user from navigating back
    }
}