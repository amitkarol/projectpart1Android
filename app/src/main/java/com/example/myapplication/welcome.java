package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.myapplication.entities.user;

public class welcome extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.applyTheme(this);
        setContentView(R.layout.welcome);

        user loggedInUser = (user) getIntent().getSerializableExtra("user");

        // Set the welcome message with the user's first name
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        if (loggedInUser != null) {
            String welcomeMessage = "Welcome, " + loggedInUser.getFirstName();
            welcomeTextView.setText(welcomeMessage);
        }

        // Delay for 2 seconds before transitioning to the homescreen activity
        new Handler().postDelayed(() -> {
            Intent homescreenIntent = new Intent(welcome.this, homescreen.class);
            homescreenIntent.putExtra("user", loggedInUser);
            startActivity(homescreenIntent);
            finish(); // Finish the welcome activity so the user cannot return to it
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
