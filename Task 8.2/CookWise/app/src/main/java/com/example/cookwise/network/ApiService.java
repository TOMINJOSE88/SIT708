package com.example.cookwise.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/generateRecipe")
    Call<Map<String, Object>> generateRecipe(@Body Map<String, String> body);
}
