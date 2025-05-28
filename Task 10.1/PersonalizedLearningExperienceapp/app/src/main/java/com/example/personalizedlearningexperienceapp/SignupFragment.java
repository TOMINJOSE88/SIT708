package com.example.personalizedlearningexperienceapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class SignupFragment extends Fragment {



    EditText usernameInput, emailInput, confirmEmailInput, passwordInput, confirmPasswordInput, phoneInput;
    Button signupButton;
    UserDatabaseHelper dbHelper;

    public SignupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        emailInput = view.findViewById(R.id.emailInput);
        confirmEmailInput = view.findViewById(R.id.confirmEmailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        phoneInput = view.findViewById(R.id.phoneInput);
        signupButton = view.findViewById(R.id.signupButton);

        dbHelper = new UserDatabaseHelper(getContext());


        signupButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String confirmEmail = confirmEmailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(confirmEmail)
                    || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(confirmEmail)) {
                Toast.makeText(getContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.isUsernameTaken(username)) {
                Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.addUser(username, email, password, phone);
            if (inserted) {
                Toast.makeText(getContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                // Optionally navigate to LoginFragment
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Signup failed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
