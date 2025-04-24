package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText username = view.findViewById(R.id.editTextUsername);
        EditText password = view.findViewById(R.id.editTextPassword);
        Button loginBtn = view.findViewById(R.id.buttonLogin);
        Button signupBtn = view.findViewById(R.id.buttonSignup);

        loginBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            boolean valid = dbHelper.validateUser(user, pass);

            if (valid) {
                Session.currentUser = user;  // ✅ Track current user globally
                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new HomeFragment())
                        .commit();
            } else {
                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SignupFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
