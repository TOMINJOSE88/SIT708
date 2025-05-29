package com.example.cookwise;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    EditText nameInput, emailInput;
    Button saveButton;

    DatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameInput = findViewById(R.id.editProfileName);
        emailInput = findViewById(R.id.editProfileEmail);
        saveButton = findViewById(R.id.saveProfileButton);

        dbHelper = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");
        String name = dbHelper.getUserNameByEmail(userEmail);

        nameInput.setText(name);
        emailInput.setText(userEmail);

        saveButton.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            String newEmail = emailInput.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = dbHelper.updateUserProfile(userEmail, newName, newEmail);
                if (updated) {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    finish(); // close activity
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
