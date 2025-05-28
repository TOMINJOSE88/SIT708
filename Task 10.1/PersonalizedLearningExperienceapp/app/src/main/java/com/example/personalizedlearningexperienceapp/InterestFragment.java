package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;

public class InterestFragment extends Fragment {

    String[] topics = {
            "Algorithms", "Data Structures", "Web Development", "Testing",
            "Cyber Security", "AI", "Machine Learning", "Cloud",
            "Databases", "DevOps"
    };

    Set<String> selectedInterests = new HashSet<>();
    GridLayout gridLayout;
    Button btnNext;

    public InterestFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        gridLayout = view.findViewById(R.id.gridLayoutInterests);
        btnNext = view.findViewById(R.id.btnNext);

        for (String topic : topics) {
            ToggleButton button = new ToggleButton(getContext());
            button.setTextOff(topic);
            button.setTextOn(topic);
            button.setText(topic);
            button.setAllCaps(false);
            button.setTextColor(Color.BLACK);
            button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.interest_selector));
            button.setPadding(24, 12, 24, 12);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(12, 12, 12, 12);
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            button.setLayoutParams(params);

            button.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    if (selectedInterests.size() < 10) {
                        selectedInterests.add(topic);
                    } else {
                        compoundButton.setChecked(false);
                        Toast.makeText(getContext(), "Max 10 topics only", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedInterests.remove(topic);
                }
            });

            gridLayout.addView(button);
        }

        btnNext.setOnClickListener(v -> {
            if (selectedInterests.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one topic", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Save interests to DB using actual logged-in username
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            if (username != null) {
                UserDatabaseHelper dbHelper = new UserDatabaseHelper(getContext());
                dbHelper.saveUserInterests(username, selectedInterests);

                Toast.makeText(getContext(), "Interests saved!", Toast.LENGTH_SHORT).show();

                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
