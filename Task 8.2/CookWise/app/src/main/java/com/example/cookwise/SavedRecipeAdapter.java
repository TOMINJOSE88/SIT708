package com.example.cookwise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedRecipeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> titles;
    private DatabaseHelper dbHelper;
    private String userEmail;

    public SavedRecipeAdapter(Context context, ArrayList<String> titles, DatabaseHelper dbHelper, String email) {
        this.context = context;
        this.titles = titles;
        this.dbHelper = dbHelper;
        this.userEmail = email;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView titleText;
        Button deleteBtn, startBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_recipe, parent, false);
            h = new ViewHolder();
            h.titleText = convertView.findViewById(R.id.recipeTitle);
            h.deleteBtn = convertView.findViewById(R.id.deleteButton);
            h.startBtn = convertView.findViewById(R.id.startButton);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        String title = titles.get(position);
        h.titleText.setText(title);

        // Delete recipe
        h.deleteBtn.setOnClickListener(v -> {
            boolean deleted = dbHelper.deleteRecipeByTitle(userEmail, title);
            if (deleted) {
                titles.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });

        // Start Cooking → open CookingModeActivity (to be created)
        h.startBtn.setOnClickListener(v -> {
            Intent i = new Intent(context, CookingModeActivity.class);
            i.putExtra("email", userEmail);
            i.putExtra("title", title);
            context.startActivity(i);
        });

        return convertView;
    }
}
