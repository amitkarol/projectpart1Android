package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class logout extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);

        // Retrieve user details from the Intent
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String username = intent.getStringExtra("username");
        String displayName = intent.getStringExtra("displayName");
        String photoUri = intent.getStringExtra("photoUri"); // Retrieve the photo URI

        // Get references to the TextView fields
        TextView textViewFirstLastName = findViewById(R.id.textView5);
        TextView textViewUsername = findViewById(R.id.textView6);
        TextView textViewDisplayName = findViewById(R.id.textView7);
        ImageView imageViewPhoto = findViewById(R.id.imageView4); // Reference to the ImageView

        // Set the user details in the TextView fields
        textViewFirstLastName.setText(firstName + " " + lastName);
        textViewUsername.setText(username);
        textViewDisplayName.setText(displayName);

        // Set the photo URI to the ImageView
        if (photoUri != null) {
            imageViewPhoto.setImageURI(Uri.parse(photoUri));
        }

        // Get reference to the Logout button
        Button logoutButton = findViewById(R.id.buttonLogout);

        // Set an OnClickListener to the Logout button
        logoutButton.setOnClickListener(v -> {
            // Navigate back to the login activity
            Intent loginIntent = new Intent(logout.this, login.class);
            // Clear the activity stack to prevent returning to the logout activity
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        });
    }
}
