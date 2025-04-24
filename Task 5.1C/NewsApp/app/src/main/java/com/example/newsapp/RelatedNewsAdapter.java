package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedViewHolder> {

    private final Context context;
    private final List<NewsItem> relatedNews;

    public RelatedNewsAdapter(Context context, List<NewsItem> relatedNews) {
        this.context = context;
        this.relatedNews = relatedNews;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_news, parent, false);
        return new RelatedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        NewsItem item = relatedNews.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getShortDescription());

        if (item.getLocalImageResId() != -1) {
            holder.image.setImageResource(item.getLocalImageResId());
        } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return relatedNews.size();
    }

    static class RelatedViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;

        public RelatedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newsImage);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.newsDescription);
        }
    }
}
