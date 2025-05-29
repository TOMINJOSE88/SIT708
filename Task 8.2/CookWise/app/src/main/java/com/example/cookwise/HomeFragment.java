package com.example.cookwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookwise.network.ApiClient;
import com.example.cookwise.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private String userEmail = "user@example.com";  // Placeholder, replace with actual user email
    private String latestRecipe = "";
    private DatabaseHelper dbHelper;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // UI references
        TextView welcomeText = view.findViewById(R.id.welcomeMessage);
        EditText inputIngredients = view.findViewById(R.id.inputIngredients);
        TextView recipeResult = view.findViewById(R.id.recipeResult);
        Button generateButton = view.findViewById(R.id.generateButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button profileButton = view.findViewById(R.id.profileButton);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button savedButton = view.findViewById(R.id.savedButton);

        dbHelper = new DatabaseHelper(getActivity());

        // Get username/email from arguments
        String username = "User";
        if (getArguments() != null) {
            username = getArguments().getString("username", "User");
            userEmail = username + "@cookwise.com"; // Assuming email is derived
        }

        welcomeText.setText("Welcome, " + username + "!");

        // Handle Generate Recipe
        generateButton.setOnClickListener(v -> {
            String ingredients = inputIngredients.getText().toString().trim();
            if (!ingredients.isEmpty()) {
                recipeResult.setText("Please wait... generating recipe...");
                Map<String, String> body = new HashMap<>();
                body.put("ingredients", ingredients);

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                apiService.generateRecipe(body).enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            latestRecipe = response.body().get("recipe").toString();
                            recipeResult.setText(latestRecipe);
                        } else {
                            recipeResult.setText("Failed to get recipe.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        recipeResult.setText("Error: " + t.getMessage());
                    }
                });

            } else {
                Toast.makeText(getActivity(), "Please enter ingredients", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Save Recipe
        saveButton.setOnClickListener(v -> {
            if (!latestRecipe.isEmpty()) {
                boolean saved = dbHelper.saveRecipe(userEmail, latestRecipe);
                if (saved) {
                    Toast.makeText(getActivity(), "Recipe saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to save recipe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No recipe to save!", Toast.LENGTH_SHORT).show();
            }
        });

        // Open Saved Recipes
        savedButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SavedRecipesActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        // Profile
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("email", userEmail); // pass email for loading profile
            startActivity(intent);
        });


        return view;
    }
}
