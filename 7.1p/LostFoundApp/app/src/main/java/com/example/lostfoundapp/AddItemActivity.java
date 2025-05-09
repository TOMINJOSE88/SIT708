package com.example.lostfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText editName, editPhone, editDescription, editDate, editLocation;
    private RadioGroup radioGroupType;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize UI elements
        radioGroupType = findViewById(R.id.radioGroupType);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        editLocation = findViewById(R.id.editLocation);
        btnSave = findViewById(R.id.btnSave);

        // Initialize DB helper
        dbHelper = new DatabaseHelper(this);

        // Save button listener
        btnSave.setOnClickListener(v -> saveAdvert());
    }

    private void saveAdvert() {
        String type = ((RadioButton) findViewById(radioGroupType.getCheckedRadioButtonId())).getText().toString();
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.insertItem(type, name, phone, description, date, location);

        if (inserted) {
            Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to main screen
        } else {
            Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show();
        }
    }
}
