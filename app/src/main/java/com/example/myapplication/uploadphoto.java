package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class uploadphoto extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadvideo);

        Button closebtn = findViewById(R.id.closeButton);
        closebtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, homescreen.class);
            startActivity(intent);
        });

    }
}
