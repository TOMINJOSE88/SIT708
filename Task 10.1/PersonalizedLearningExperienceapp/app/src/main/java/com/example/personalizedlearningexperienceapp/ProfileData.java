package com.example.personalizedlearningexperienceapp;

public class ProfileData {
    private String username;
    private String email;
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;

    public ProfileData(String username, String email, int totalQuestions, int correctAnswers, int incorrectAnswers) {
        this.username = username;
        this.email = email;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
    }

    // (Optional) Add getters/setters if needed by Gson or for testing
}
