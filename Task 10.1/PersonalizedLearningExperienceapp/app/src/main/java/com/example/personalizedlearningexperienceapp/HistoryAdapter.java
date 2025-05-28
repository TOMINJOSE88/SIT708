package com.example.personalizedlearningexperienceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<QuizHistoryItem> historyList;

    public HistoryAdapter(List<QuizHistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizHistoryItem item = historyList.get(position);

        holder.tvQuestionTitle.setText((position + 1) + ". " + item.question);
        holder.tvExplanation.setText(item.explanation);
        holder.tvYourAnswer.setText("Your Answer: " + item.userAnswer);
        holder.tvCorrectAnswer.setText("Correct Answer: " + item.correctAnswer);

        // Color coding
        if (item.userAnswer.equals(item.correctAnswer)) {
            holder.tvYourAnswer.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvYourAnswer.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark));
        }

        holder.tvCorrectAnswer.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));

        // Toggle visibility
        holder.toggleButton.setOnClickListener(v -> {
            if (holder.detailsLayout.getVisibility() == View.GONE) {
                holder.detailsLayout.setVisibility(View.VISIBLE);
                holder.toggleIcon.setRotation(180f);
            } else {
                holder.detailsLayout.setVisibility(View.GONE);
                holder.toggleIcon.setRotation(0f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTitle, tvExplanation, tvYourAnswer, tvCorrectAnswer;
        ImageView toggleIcon;
        LinearLayout detailsLayout, toggleButton;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionTitle = itemView.findViewById(R.id.tvQuestionTitle);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
            tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            toggleIcon = itemView.findViewById(R.id.ivToggle);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            toggleButton = itemView.findViewById(R.id.toggleLayout);
        }
    }
}
