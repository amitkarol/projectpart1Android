package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.entities.user;
import com.example.myapplication.entities.UserManager;

public class Displayname extends BaseActivity {

    private static final String TAG = "DisplaynameActivity";
    private static final int REQUEST_IMAGE_PICK = 102;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText displaynameEdittext;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            ThemeUtil.applyTheme(this);
            setContentView(R.layout.displayname);

            displaynameEdittext = findViewById(R.id.editTextText2);

            Button continueButton = findViewById(R.id.button3);
            continueButton.setOnClickListener(v -> {
                String displayname = displaynameEdittext.getText().toString().trim();

                if (displayname.isEmpty()) {
                    Toast.makeText(Displayname.this, "Please enter a display name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedImageUri == null) {
                    Toast.makeText(Displayname.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = getIntent();
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                String firstName = intent.getStringExtra("firstName");
                String lastName = intent.getStringExtra("lastName");

                user user = new user(firstName, lastName, username, password, displayname, selectedImageUri.toString());
                UserManager.getInstance().addUser(user);

                Intent homescreenIntent = new Intent(Displayname.this, homescreen.class);
                homescreenIntent.putExtra("user", user);

                Log.d(TAG, "Switching to homescreen activity with user: " + user.toString());

                startActivity(homescreenIntent);
            });
            Button buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);
            buttonUploadPhoto.setOnClickListener(v -> checkAndRequestPermissionsForGallery());

            Log.d(TAG, "onCreate: Activity created and theme applied.");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error applying theme", e);
        }
    }

    private void checkAndRequestPermissionsForGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                selectedImageUri = data.getData();
                displaySelectedImage();
            }
        }
    }

    private void displaySelectedImage() {
        if (selectedImageUri != null) {
            ImageView imageViewPhoto = findViewById(R.id.imageViewPhoto);
            imageViewPhoto.setImageURI(selectedImageUri);
            imageViewPhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean readExternalStorageGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (readExternalStorageGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
