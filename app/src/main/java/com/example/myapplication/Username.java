
package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Username extends Activity {  // Ensure this class is public
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username);

        usernameEditText = findViewById(R.id.editTextText2);

        Button secbtnName = findViewById(R.id.second_button);
        secbtnName.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();

            if (!isValidEmail(username)) {
                // Display error message for invalid email format
                Toast.makeText(this, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to next screen
                Intent intent = new Intent(this, password.class);
                intent.putExtra("firstName" ,  getIntent().getStringExtra("firstName"));
                intent.putExtra("lastName" , getIntent().getStringExtra("lastName"));
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
