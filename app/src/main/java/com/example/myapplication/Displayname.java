package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myapplication.entities.user;
import com.example.myapplication.entities.UserManager;
import java.io.ByteArrayOutputStream;

public class Displayname extends BaseActivity {

    private static final int REQUEST_IMAGE_PICK = 102;
    private static final int REQUEST_IMAGE_CAPTURE = 123;

    private EditText displaynameEdittext;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.applyTheme(this);
        setContentView(R.layout.displayname);

        displaynameEdittext = findViewById(R.id.editTextText2);

        Button continueButton = findViewById(R.id.button3);
        continueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.custom_red)));

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

            startActivity(homescreenIntent);
        });

        Button buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);
        buttonUploadPhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.custom_red)));

        buttonUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        Button buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.custom_red)));

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                selectedImageUri = data.getData();
                ImageView imageView = findViewById(R.id.imageViewPhoto);
                imageView.setImageURI(selectedImageUri);
                imageView.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                ImageView imageView = findViewById(R.id.imageViewPhoto);
                imageView.setImageBitmap(photo);
                imageView.setVisibility(View.VISIBLE);
                selectedImageUri = getImageUri(this , photo);
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

