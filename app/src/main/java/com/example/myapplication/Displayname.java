package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.entities.user;
import com.example.myapplication.entities.UserManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Displayname extends Activity {

    private static final int REQUEST_IMAGE_PICK = 102;
    private static final int REQUEST_IMAGE_CAPTURE = 103;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText displaynameEdittext;
    private Uri selectedImageUri;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayname);

        displaynameEdittext = findViewById(R.id.editTextText2);

        Button continueButton = findViewById(R.id.button3);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayname = displaynameEdittext.getText().toString().trim();

                if (displayname.isEmpty() && selectedImageUri == null) {
                    Toast.makeText(Displayname.this, "Please enter a display name and upload a photo", Toast.LENGTH_SHORT).show();
                    return;
                } else if (displayname.isEmpty()) {
                    Toast.makeText(Displayname.this, "Please enter a display name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (selectedImageUri == null) {
                    Toast.makeText(Displayname.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = getIntent();
                String photoPath = photoUri.toString();
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");
                String firstName = intent.getStringExtra("firstName");
                String lastName = intent.getStringExtra("lastName");

                user user = new user(firstName, lastName, username, password, displayname, photoPath);
                UserManager.getInstance().addUser(user);

                Intent homescreenIntent = new Intent(Displayname.this, homescreen.class);
                homescreenIntent.putExtra("firstName", getIntent().getStringExtra("firstName"));
                homescreenIntent.putExtra("lastName", getIntent().getStringExtra("lastName"));
                homescreenIntent.putExtra("username", getIntent().getStringExtra("username"));
                homescreenIntent.putExtra("password", getIntent().getStringExtra("password"));
                homescreenIntent.putExtra("displayName", displayname);
                homescreenIntent.putExtra("photoUri", selectedImageUri.toString());
                startActivity(homescreenIntent);
            }
        });

        Button buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissionsForCamera();
            }
        });
    }

    private void checkAndRequestPermissionsForCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error occurred while creating the photo file", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                selectedImageUri = photoUri;
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
                boolean cameraGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeExternalStorageGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (cameraGranted && writeExternalStorageGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera or storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}