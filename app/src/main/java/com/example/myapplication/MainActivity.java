package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationscreen);

        firstNameEditText = findViewById(R.id.editTextText);
        lastNameEditText = findViewById(R.id.editTextText2);

        Button btnName = findViewById(R.id.first_reg_button);
        btnName.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();

            if (!isValidName(firstName) || !isValidName(lastName)) {
                // Display error message for invalid name format
                Toast.makeText(this, "Invalid name format. Please enter only letters.", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to next screen
                Intent intent = new Intent(MainActivity.this, Username.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                startActivity(intent);
            }
        });
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z]+");
    }
}