package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

public class uploadvideo extends Activity {

    private static final int REQUEST_VIDEO_PICK = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadvideo);

        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, homescreen.class);
            startActivity(intent);
        });

        Button uploadButton = findViewById(R.id.uploadVideoButton);
        uploadButton.setOnClickListener(v -> {
            openGallery();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // Handle the selected video here
            // You can get the video URI from the data intent
            // For example:
            // Uri selectedVideoUri = data.getData();
        }
    }
}
