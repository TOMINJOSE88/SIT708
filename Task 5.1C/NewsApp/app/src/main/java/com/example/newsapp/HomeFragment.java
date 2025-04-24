package com.example.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView topStoriesRecycler, latestNewsRecycler;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        topStoriesRecycler = view.findViewById(R.id.topStoriesRecycler);
        latestNewsRecycler = view.findViewById(R.id.latestNewsRecycler);

        List<NewsItem> topStories = DummyData.getDummyTopStories();
        List<NewsItem> latestNews = DummyData.getDummyLatestNews();


        setupRecycler(topStoriesRecycler, topStories, true);
        setupRecycler(latestNewsRecycler, latestNews, false);
    }

    private void setupRecycler(RecyclerView recyclerView, List<NewsItem> newsList, boolean isHorizontal) {
        RecyclerView.LayoutManager layoutManager = isHorizontal ?
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false) :
                new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);

        NewsAdapter adapter = new NewsAdapter(getContext(), newsList, item -> {
            Fragment fragment;

            
            if (item.getLocalImageResId() != -1) {
                fragment = NewsDetailFragment.newInstance(
                        item.getTitle(),
                        item.getLocalImageResId(),
                        item.getDescription()
                );
            } else {
                fragment = NewsDetailFragment.newInstance(
                        item.getTitle(),
                        item.getLocalImageResId(),
                        item.getDescription()
                );
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);
    }
}
