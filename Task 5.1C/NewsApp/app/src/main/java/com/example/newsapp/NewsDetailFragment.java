package com.example.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_DESC = "description";

    public static NewsDetailFragment newInstance(String title, int imageResId, String description) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_IMAGE, imageResId);
        args.putString(ARG_DESC, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView detailImage = view.findViewById(R.id.detailImage);
        TextView detailTitle = view.findViewById(R.id.detailTitle);
        TextView detailDescription = view.findViewById(R.id.detailDescription);
        RecyclerView relatedRecycler = view.findViewById(R.id.relatedNewsRecycler);

        assert getArguments() != null;
        String title = getArguments().getString(ARG_TITLE);
        int imageResId = getArguments().getInt(ARG_IMAGE);
        String description = getArguments().getString(ARG_DESC);

        detailImage.setImageResource(imageResId);
        detailTitle.setText(title);
        detailDescription.setText(description);

        // Load all dummy news except current one
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem item : DummyData.getDummyLatestNews()) {
            if (!item.getTitle().equals(title)) {
                related.add(item);
            }
        }

        relatedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        relatedRecycler.setAdapter(new RelatedNewsAdapter(getContext(), related));
    }
}
