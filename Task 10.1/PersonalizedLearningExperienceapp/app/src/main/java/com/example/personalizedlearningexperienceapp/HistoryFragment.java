package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<QuizHistoryItem> historyList = new ArrayList<>();

        // ✅ Load history from SQLite
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username != null) {
            UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
            List<String[]> rawHistory = dbHelper.getQuizHistory(username);

            for (String[] item : rawHistory) {
                String question = item[0];
                String yourAnswer = item[1];
                String correctAnswer = item[2];
                String feedback;

                if (yourAnswer.equals(correctAnswer)) {
                    feedback = "✅ Correct!";
                } else if (yourAnswer.equals("Not Answered")) {
                    feedback = "⚠️ Not attempted.";
                } else {
                    feedback = "❌ Incorrect.";
                }

                historyList.add(new QuizHistoryItem(question, yourAnswer, correctAnswer, feedback));
            }
        }

        HistoryAdapter adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        // Back button click
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}
