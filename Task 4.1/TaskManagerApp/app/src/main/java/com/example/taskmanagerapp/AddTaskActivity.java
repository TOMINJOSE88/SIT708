package com.example.taskmanagerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonSave;

    private TaskDatabase taskDatabase;
    private int taskId = -1; // ✅ used for editing check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        buttonSave = findViewById(R.id.button_save);

        taskDatabase = TaskDatabase.getInstance(this);

        // 🗓️ Set up date picker only
        editTextDueDate.setFocusable(false);
        editTextDueDate.setClickable(true);
        editTextDueDate.setOnClickListener(v -> showDatePicker());

        // Check if editing
        Intent intent = getIntent();
        if (intent.hasExtra("task_id")) {
            taskId = intent.getIntExtra("task_id", -1);
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextDescription.setText(intent.getStringExtra("description"));
            editTextDueDate.setText(intent.getStringExtra("due_date"));
            buttonSave.setText("Update Task");
        }

        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTaskActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editTextDueDate.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(dueDate)) {
            Toast.makeText(this, "Title and Due Date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (taskId != -1) {
            Task task = new Task(title, description, dueDate);
            task.setId(taskId);
            new Thread(() -> {
                taskDatabase.taskDao().update(task);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } else {
            Task task = new Task(title, description, dueDate);
            new Thread(() -> {
                taskDatabase.taskDao().insert(task);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        }
    }
}