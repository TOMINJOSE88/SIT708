package com.example.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        taskAdapter = new TaskAdapter();
        recyclerView.setAdapter(taskAdapter);

        taskDatabase = TaskDatabase.getInstance(this);

        findViewById(R.id.button_add_task).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
        });

        taskAdapter.setOnItemLongClickListener(task -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            taskDatabase.taskDao().delete(task);
                            runOnUiThread(this::loadTasks);
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        taskAdapter.setOnItemClickListener(task -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("title", task.getTitle());
            intent.putExtra("description", task.getDescription());
            intent.putExtra("due_date", task.getDueDate());
            startActivity(intent);
        });

        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        new Thread(() -> {
            List<Task> tasks = taskDatabase.taskDao().getAllTasks();
            runOnUiThread(() -> taskAdapter.setTasks(tasks));
        }).start();
    }
}
