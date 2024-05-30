package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

        // Get references to the EditText fields
        EditText editTextFirstLastName = findViewById(R.id.editTextText5);
        EditText editTextUsername = findViewById(R.id.editTextText6);
        EditText editTextDisplayName = findViewById(R.id.editTextText7);

        // Set the user details in the EditText fields
        editTextFirstLastName.setText(firstName + " " + lastName);
        editTextUsername.setText(username);
        editTextDisplayName.setText(displayName);


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
