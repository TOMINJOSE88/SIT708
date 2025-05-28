package com.example.personalizedlearningexperienceapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;


public interface QuizApiService {
    @GET("getQuiz")
    Call<QuizResponse> getQuiz(@Query("topic") String topic);
}

