package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistFragment extends Fragment {

    private PlaylistAdapter adapter;

    public PlaylistFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPlaylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        adapter = new PlaylistAdapter(
                dbHelper.getAllUrls(),
                new PlaylistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String url) {
                        HomeFragment.currentVideoUrl = url;
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new PlayerFragment())
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onDeleteClick(String url) {
                        dbHelper.deleteUrl(url);
                        adapter.updateList(dbHelper.getAllUrls()); // Refresh the list
                    }
                }
        );

        recyclerView.setAdapter(adapter);
    }
}
