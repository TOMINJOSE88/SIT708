package com.example.personalizedlearningexperienceapp;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileApiService {
    @POST("/api/profile")
    Call<Void> uploadProfile(@Body ProfileData profile);
}
