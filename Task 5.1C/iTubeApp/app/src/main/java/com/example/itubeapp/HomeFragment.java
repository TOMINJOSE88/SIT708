package com.example.itubeapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public static String currentVideoUrl = "";

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText urlInput = view.findViewById(R.id.editTextUrl);
        Button playBtn = view.findViewById(R.id.buttonPlay);
        Button addBtn = view.findViewById(R.id.buttonAdd);
        Button playlistBtn = view.findViewById(R.id.buttonPlaylist);

        playBtn.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "Enter a valid YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }

            currentVideoUrl = url;

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new PlayerFragment())
                    .addToBackStack(null)
                    .commit();
        });

        addBtn.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "URL can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Session.currentUser == null) {
                Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.insertUrl(url);
            Toast.makeText(getContext(), "Saved to Playlist", Toast.LENGTH_SHORT).show();
        });

        playlistBtn.setOnClickListener(v -> {
            if (Session.currentUser == null) {
                Toast.makeText(getContext(), "Login required to view playlist", Toast.LENGTH_SHORT).show();
                return;
            }

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new PlaylistFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
