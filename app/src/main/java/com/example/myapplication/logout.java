package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.entities.user;

public class logout extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.applyTheme(this);
        setContentView(R.layout.logout);

        Uri person = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.person);
        // Retrieve user details from the Intent
        user loggedInUser = (user) getIntent().getSerializableExtra("user");

        // Check if the user is null
        if (loggedInUser == null) {
            // Handle the case where user data is not passed
            loggedInUser = new user("Test", "User", "testuser@example.com", "Password@123", "TestUser", person.toString());
        }

        // Get references to the TextView fields
        TextView textViewFirstLastName = findViewById(R.id.textView5);
        TextView textViewUsername = findViewById(R.id.textView6);
        TextView textViewDisplayName = findViewById(R.id.textView7);
        ImageView imageViewPhoto = findViewById(R.id.imageView4); // Reference to the ImageView

        // Set the user details in the TextView fields
        textViewFirstLastName.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        textViewUsername.setText(loggedInUser.getEmail());
        textViewDisplayName.setText(loggedInUser.getDisplayName());

        // Set the photo URI to the ImageView
        if (loggedInUser.getPhotoUri() != null) {
            imageViewPhoto.setImageURI(Uri.parse(loggedInUser.getPhotoUri()));
        }

        // Get reference to the Logout button
        Button logoutButton = findViewById(R.id.buttonLogout);
        user testUser = new user("Test", "User", "testuser@example.com", "Password@123", "TestUser", person.toString());

        // Set an OnClickListener to the Logout button
        logoutButton.setOnClickListener(v -> {
            // Navigate back to the login activity
            Intent homescreenIntent = new Intent(logout.this, homescreen.class);
            homescreenIntent.putExtra("user" , testUser);
            // Clear the activity stack to prevent returning to the logout activity
            homescreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homescreenIntent);
        });
    }
}
