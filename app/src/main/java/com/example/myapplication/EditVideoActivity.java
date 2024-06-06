package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.entities.VideoManager;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;

public class EditVideoActivity extends Activity {
    private static final int REQUEST_VIDEO_PICK = 101;
    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private video currentVideo;
    private user loggedInUser;
    private Uri selectedVideoUri;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_video);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button replaceButton = findViewById(R.id.replaceButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button pickThumbnailButton = findViewById(R.id.pickThumbnailButton);

        // Retrieve data from the intent
        Intent intent = getIntent();
        currentVideo = (video) intent.getSerializableExtra("video");
        loggedInUser = (user) intent.getSerializableExtra("user");
        video originVideo = new video(currentVideo);

        // Check if the video belongs to the logged-in user
        if (currentVideo != null && !currentVideo.getChannelName().equals(loggedInUser.getDisplayName())) {
            Toast.makeText(this, "You can only edit your own videos.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields if data is available
        if (currentVideo != null) {
            titleEditText.setText(currentVideo.getTitle());
            descriptionEditText.setText(currentVideo.getDescription());
        }

        // Set the save button listener
        saveButton.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Save")
                    .setMessage("Are you sure you want to save the changes?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Get the new title and description from the fields
                        String newTitle = titleEditText.getText().toString().trim();
                        String newDescription = descriptionEditText.getText().toString().trim();

                        // Update the video object
                        currentVideo.setTitle(newTitle);
                        currentVideo.setDescription(newDescription);
                        if (selectedVideoUri != null) {
                            currentVideo.setVideoUrl(selectedVideoUri.toString());
                        }
                        if (selectedImageUri != null) {
                            currentVideo.setThumbnailUrl(selectedImageUri.toString());
                        }

                        // Update the video in VideoManager
                        VideoManager.getInstance().updateVideo(currentVideo ,originVideo );

                        // Show a confirmation message
                        Toast.makeText(this, "Video updated", Toast.LENGTH_SHORT).show();

                        // Navigate back to the homescreen
                        Intent homeIntent = new Intent(this, homescreen.class);
                        homeIntent.putExtra("user", loggedInUser);
                        startActivity(homeIntent);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        // Set the replace button listener
        replaceButton.setOnClickListener(v -> {
            Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickVideoIntent, REQUEST_VIDEO_PICK);
        });

        // Set the delete button listener
        deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog for deletion
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this video?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Remove the video from VideoManager
                        VideoManager.getInstance().removeVideo(currentVideo);

                        // Show a confirmation message
                        Toast.makeText(this, "Video deleted", Toast.LENGTH_SHORT).show();

                        // Navigate back to the homescreen
                        Intent homeIntent = new Intent(this, homescreen.class);
                        homeIntent.putExtra("user", loggedInUser);
                        startActivity(homeIntent);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        // Set the pick thumbnail button listener
        pickThumbnailButton.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_PICK && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            Toast.makeText(this, "New video selected", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(this, "New thumbnail selected", Toast.LENGTH_SHORT).show();
        }
    }
}
