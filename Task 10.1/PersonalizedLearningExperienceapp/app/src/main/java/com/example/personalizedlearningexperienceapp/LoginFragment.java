package com.example.personalizedlearningexperienceapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.content.Context;


public class LoginFragment extends Fragment {

    EditText usernameInput, passwordInput;
    Button loginButton;
    TextView goToSignupText;
    UserDatabaseHelper dbHelper;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameInput = view.findViewById(R.id.loginUsernameInput);
        passwordInput = view.findViewById(R.id.loginPasswordInput);
        loginButton = view.findViewById(R.id.loginButton);
        goToSignupText = view.findViewById(R.id.goToSignupText);
        dbHelper = new UserDatabaseHelper(getContext());

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(username, password)) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        goToSignupText.setOnClickListener(v -> {
            // Switch to SignupFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SignupFragment())
                    .commit();
        });

        return view;
    }
}
