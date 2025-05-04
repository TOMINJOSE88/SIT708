package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizFragment extends Fragment {

    private RecyclerView quizRecyclerView;
    private Button submitButton;
    private QuizAdapter quizAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        quizRecyclerView = view.findViewById(R.id.quizRecyclerView);
        submitButton = view.findViewById(R.id.submitButton);
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list
        quizAdapter = new QuizAdapter(new ArrayList<>());
        quizRecyclerView.setAdapter(quizAdapter);

        // Load user topic from shared preferences
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);

        String topic = "AI"; // Default fallback
        if (username != null) {
            UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
            List<String> interests = dbHelper.getUserInterests(username);
            if (interests != null && !interests.isEmpty()) {
                topic = interests.get(new Random().nextInt(interests.size()));
            }
        }

        // Setup Retrofit with timeout
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuizApiService api = retrofit.create(QuizApiService.class);

        // Fetch quiz
        api.getQuiz(topic).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuizQuestion> quizList = response.body().getQuiz();
                    quizAdapter.updateData(quizList);  // Update the quiz list only once
                } else {
                    Log.e("QuizFragment", "Invalid response from server");
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Log.e("QuizFragment", "Retrofit error: " + t.getMessage());
            }
        });

        submitButton.setOnClickListener(v -> {
            if (quizAdapter != null) {
                int score = 0;
                int[] answers = quizAdapter.getSelectedAnswers();
                List<QuizQuestion> questions = quizAdapter.getQuizList();

                for (int i = 0; i < answers.length; i++) {
                    if (answers[i] != -1) {
                        String correctLetter = questions.get(i).getCorrectAnswer().trim().toUpperCase();
                        int correctIndex = correctLetter.charAt(0) - 'A';

                        if (answers[i] == correctIndex) {
                            score++;
                        }
                    }

                }

                ResultFragment resultFragment = new ResultFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("score", score);
                bundle.putInt("total", questions.size());
                resultFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, resultFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
