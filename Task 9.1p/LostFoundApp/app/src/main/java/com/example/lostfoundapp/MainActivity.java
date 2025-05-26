package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAdvert, btnShowItems, btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);

        // Navigate to AddItemActivity
        btnCreateAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        // Navigate to ItemListActivity
        btnShowItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(intent);
        });

        // Navigate to MapActivity
        btnShowOnMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }
}
