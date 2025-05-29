package com.example.cookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SavedRecipesActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView listView;
    TextView emptyText;
    String userEmail;
    ArrayList<String> recipeTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        listView = findViewById(R.id.recipeListView);
        emptyText = findViewById(R.id.emptyText);
        dbHelper = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");

        recipeTitles = dbHelper.getRecipeTitles(userEmail);
        if (recipeTitles.isEmpty()) {
            emptyText.setText("No recipes saved yet!");
        } else {
            emptyText.setVisibility(TextView.GONE);
            SavedRecipeAdapter adapter = new SavedRecipeAdapter(this, recipeTitles, dbHelper, userEmail);
            listView.setAdapter(adapter);
        }
    }
}
