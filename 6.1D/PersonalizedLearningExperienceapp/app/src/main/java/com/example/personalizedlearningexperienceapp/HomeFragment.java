package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Random;


import java.util.List;

public class HomeFragment extends Fragment {

    TextView greetingText, tvMyInterests;
    Button btnSetInterest, btnLogout;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        greetingText = view.findViewById(R.id.greetingText);
        tvMyInterests = view.findViewById(R.id.tvMyInterests);
        btnSetInterest = view.findViewById(R.id.btnSetInterest);
        btnLogout = view.findViewById(R.id.btnLogout);
        Button btnGenerateTaskArrow = view.findViewById(R.id.btnGenerateTaskArrow);

        greetingText.setText("Hello, Student!");

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", null);

        UserDatabaseHelper dbHelper = new UserDatabaseHelper(getContext());

        if (currentUsername != null) {
            List<String> interests = dbHelper.getUserInterests(currentUsername);
            if (!interests.isEmpty()) {
                StringBuilder sb = new StringBuilder("My Interests: ");
                for (String interest : interests) {
                    sb.append(interest).append(", ");
                }
                sb.setLength(sb.length() - 2); // remove trailing comma
                tvMyInterests.setText(sb.toString());
            } else {
                tvMyInterests.setText("My Interests: None selected yet");
            }
        } else {
            tvMyInterests.setText("My Interests: [User not found]");
        }

        btnGenerateTaskArrow.setOnClickListener(v -> {
            SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String username = prefs.getString("username", null);

            if (username != null) {
                List<String> interests = dbHelper.getUserInterests(username);  // ✅ reuse existing dbHelper

                if (!interests.isEmpty()) {
                    String selectedTopic = interests.get(new Random().nextInt(interests.size()));

                    // Pass the topic to QuizFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("topic", selectedTopic);

                    QuizFragment quizFragment = new QuizFragment();
                    quizFragment.setArguments(bundle);

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, quizFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "No interests found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });


        btnSetInterest.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new InterestFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnLogout.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        });

        return view;
    }
}
