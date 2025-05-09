package com.example.lostfoundapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemAdapter adapter;
    DatabaseHelper dbHelper;
    ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.recyclerView);
        dbHelper = new DatabaseHelper(this);
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(this, itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadItems(); // Initial load
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // Refresh on return
    }

    private void loadItems() {
        itemList.clear(); // Clear previous data

        Cursor cursor = dbHelper.getAllItems();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                String name = cursor.getString(2);
                String phone = cursor.getString(3);
                String description = cursor.getString(4);
                String date = cursor.getString(5);
                String location = cursor.getString(6);

                itemList.add(new Item(id, type, name, phone, description, date, location));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No items found", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged(); // Refresh RecyclerView
    }
}
