package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.entities.UserManager;

public class login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        // Button for creating an account
        Button btncreate = findViewById(R.id.create_account);
        btncreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Button for logging in
        Button btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(v -> {
            // Get the entered username and password
            EditText editTextUsername = findViewById(R.id.editTextUsername);
            EditText editTextPassword = findViewById(R.id.editTextPassword);

            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Validate the username and password
            boolean userFound = UserManager.getInstance().validateUser(username, password);


            if (userFound) {
                // Open the home screen activity
                Intent intent = new Intent(this, homescreen.class);
                startActivity(intent);
            } else {
                // Display error message
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
