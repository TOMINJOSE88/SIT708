package com.example.personalizedlearningexperienceapp;
public class QuizHistoryItem {
    public String question;
    public String userAnswer;
    public String correctAnswer;
    public String explanation;

    public QuizHistoryItem(String question, String userAnswer, String correctAnswer, String explanation) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }
}
