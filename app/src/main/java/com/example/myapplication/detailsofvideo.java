package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;

import java.util.UUID;

public class detailsofvideo extends BaseActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSaveDetails;
    private Button buttonPickThumbnail;
    private ImageView imageViewThumbnail;

    private video selectedVideo;
    private user user;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.applyTheme(this);
        setContentView(R.layout.detailsofvideo);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSaveDetails = findViewById(R.id.buttonSaveDetails);
        buttonPickThumbnail = findViewById(R.id.buttonPickThumbnail);
        imageViewThumbnail = findViewById(R.id.imageViewThumbnail);

        // Retrieve the video URL and user details from the Intent
        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("videoUrl");
        user = (user) intent.getSerializableExtra("user");

        selectedVideo = new video(UUID.randomUUID().toString(), "", "", "", 0, videoUrl, user, 0, 0);

        buttonPickThumbnail.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
        });

        buttonSaveDetails.setOnClickListener(v -> {
            // Save video details
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();

            if (title.isEmpty() || description.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Please enter title, description, and select a thumbnail", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedVideo.setTitle(title);
            selectedVideo.setDescription(description);
            selectedVideo.setThumbnailUrl(selectedImageUri.toString());

            // Add the video to VideoManager
            VideoManager.getInstance().addVideo(selectedVideo);

            // Save the video details and go back to the home screen
            Intent homeIntent = new Intent(detailsofvideo.this, homescreen.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewThumbnail.setImageURI(selectedImageUri);
        }
    }
}
