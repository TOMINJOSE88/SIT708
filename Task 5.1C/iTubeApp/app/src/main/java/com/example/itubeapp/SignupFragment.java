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

public class SignupFragment extends Fragment {

    public SignupFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fullName = view.findViewById(R.id.editTextFullName);
        EditText username = view.findViewById(R.id.editTextUsername);
        EditText password = view.findViewById(R.id.editTextPassword);
        EditText confirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        Button createAccount = view.findViewById(R.id.buttonCreateAccount);

        createAccount.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            boolean inserted = dbHelper.insertUser(name, user, pass);

            if (inserted) {
                Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
