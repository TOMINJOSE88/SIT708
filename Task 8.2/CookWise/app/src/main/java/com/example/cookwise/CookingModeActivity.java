package com.example.cookwise;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CookingModeActivity extends AppCompatActivity {

    TextView recipeDisplay;
    DatabaseHelper dbHelper;
    String userEmail, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_mode);

        recipeDisplay = findViewById(R.id.recipeFullText);
        dbHelper = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");
        title = getIntent().getStringExtra("title");

        ArrayList<String> allRecipes = dbHelper.getSavedRecipes(userEmail);
        for (String recipe : allRecipes) {
            if (recipe.startsWith("Recipe Name:") && recipe.contains(title)) {
                recipeDisplay.setText(recipe);
                return;
            }
        }

        recipeDisplay.setText("Recipe not found.");
    }
}
