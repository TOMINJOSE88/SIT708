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
    private String username;

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
        quizAdapter = new QuizAdapter(new ArrayList<>());
        quizRecyclerView.setAdapter(quizAdapter);

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        username = prefs.getString("username", null);

        String topic = "AI";
        if (username != null) {
            UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
            List<String> interests = dbHelper.getUserInterests(username);
            if (interests != null && !interests.isEmpty()) {
                topic = interests.get(new Random().nextInt(interests.size()));
            }
        }

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

        api.getQuiz(topic).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuizQuestion> quizList = response.body().getQuiz();
                    quizAdapter.updateData(quizList);
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
                UserDatabaseHelper dbHelper = new UserDatabaseHelper(getContext());

                for (int i = 0; i < answers.length; i++) {
                    QuizQuestion q = questions.get(i);
                    String questionText = q.getQuestion();
                    String correctLetter = q.getCorrectAnswer().trim().toUpperCase();
                    int correctIndex = correctLetter.charAt(0) - 'A';
                    String correctAnswer = q.getOptions().get(correctIndex);
                    String selectedAnswer = (answers[i] != -1) ? q.getOptions().get(answers[i]) : "Not Answered";

                    // Count score
                    if (answers[i] == correctIndex) {
                        score++;
                    }

                    // Save to SQLite
                    if (username != null) {
                        dbHelper.addQuizHistory(username, questionText, selectedAnswer, correctAnswer);
                    }
                }

                // Go to result screen
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
