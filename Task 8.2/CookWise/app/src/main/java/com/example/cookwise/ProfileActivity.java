package com.example.cookwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameText, userEmailText;
    Button editButton, shareButton;
    DatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameText = findViewById(R.id.userNameText);
        userEmailText = findViewById(R.id.userEmailText);
        editButton = findViewById(R.id.editButton);
        shareButton = findViewById(R.id.shareRecipesButton);

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("email");

        // Set user details
        String userName = dbHelper.getUserNameByEmail(userEmail);
        userNameText.setText("Name: " + userName);
        userEmailText.setText("Email: " + userEmail);

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        shareButton.setOnClickListener(v -> {
            String recipes = dbHelper.getAllRecipesAsString(userEmail);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, recipes);
            startActivity(Intent.createChooser(shareIntent, "Share Recipes"));
        });
    }
}
