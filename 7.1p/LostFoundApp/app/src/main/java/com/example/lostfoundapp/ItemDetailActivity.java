package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    TextView txtDescription, txtDate, txtLocation;
    Button btnDelete;
    DatabaseHelper dbHelper;
    int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        txtDescription = findViewById(R.id.txtDescription);
        txtDate = findViewById(R.id.txtDate);
        txtLocation = findViewById(R.id.txtLocation);
        btnDelete = findViewById(R.id.btnDelete);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("item")) {
            Item item = (Item) intent.getSerializableExtra("item");
            itemId = item.id;
            txtDescription.setText(item.type + " " + item.description);
            txtDate.setText(item.date);
            txtLocation.setText("At " + item.location);
        }

        btnDelete.setOnClickListener(v -> {
            if (itemId != -1) {
                boolean success = dbHelper.deleteItem(itemId);
                if (success) {
                    Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to remove", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
