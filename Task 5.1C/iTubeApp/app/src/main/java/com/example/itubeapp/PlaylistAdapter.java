package com.example.itubeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<String> playlist;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String url);
        void onDeleteClick(String url);
    }

    public PlaylistAdapter(List<String> playlist, OnItemClickListener listener) {
        this.playlist = playlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        String url = playlist.get(position);
        holder.textView.setText(url);
        holder.textView.setOnClickListener(v -> listener.onItemClick(url));
        holder.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(url));
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public void updateList(List<String> newList) {
        this.playlist = newList;
        notifyDataSetChanged();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button deleteBtn;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textUrl);
            deleteBtn = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
