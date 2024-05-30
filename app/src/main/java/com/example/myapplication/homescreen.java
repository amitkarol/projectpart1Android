package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class homescreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // Find the buttonPerson button by its ID
        Button buttonPerson = findViewById(R.id.buttonPerson);

        // Set an OnClickListener to the buttonPerson button
        buttonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user details from the Intent
                Intent intent = getIntent();
                String firstName = intent.getStringExtra("firstName");
                String lastName = intent.getStringExtra("lastName");
                String username = intent.getStringExtra("username");
                String displayName = intent.getStringExtra("displayName");

                // Start the logout activity with user details as extras
                Intent logoutIntent = new Intent(homescreen.this, logout.class);
                logoutIntent.putExtra("firstName", firstName);
                logoutIntent.putExtra("lastName", lastName);
                logoutIntent.putExtra("username", username);
                logoutIntent.putExtra("displayName", displayName);
                startActivity(logoutIntent);
            }
        });

        Button buttonPlus = findViewById(R.id.buttonUpload);
        buttonPlus.setOnClickListener( v-> {
            Intent intent = new Intent(this, uploadphoto.class);
            startActivity(intent);
        });

        // Find the switchMode switch by its ID
        Switch switchMode = findViewById(R.id.switchMode);

        // Set an OnCheckedChangeListener to the switchMode switch
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDarkMode(isChecked);
            }
        });
    }

    private void setDarkMode(boolean darkModeEnabled) {
        int newNightMode = darkModeEnabled ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        getApplication().getResources().getConfiguration().uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        getApplication().getResources().getConfiguration().uiMode |= newNightMode;

        recreate();
    }


}
