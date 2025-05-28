package com.example.personalizedlearningexperienceapp;

import java.util.List;

public class QuizQuestion {
    private String question;
    private List<String> options;
    private String correct_answer;

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }
}


