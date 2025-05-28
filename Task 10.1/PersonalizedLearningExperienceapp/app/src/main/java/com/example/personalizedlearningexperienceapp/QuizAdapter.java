package com.example.personalizedlearningexperienceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<QuizQuestion> quizList;
    private int[] selectedAnswers;

    public QuizAdapter(List<QuizQuestion> quizList) {
        this.quizList = quizList != null ? quizList : new ArrayList<>();
        this.selectedAnswers = new int[this.quizList.size()];
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }
    }

    // ✅ Update data method
    public void updateData(List<QuizQuestion> newList) {
        this.quizList = newList != null ? newList : new ArrayList<>();
        this.selectedAnswers = new int[this.quizList.size()];
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }
        notifyDataSetChanged();
    }

    public int[] getSelectedAnswers() {
        return selectedAnswers;
    }

    public List<QuizQuestion> getQuizList() {
        return quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_card, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizQuestion question = quizList.get(position);
        holder.questionText.setText(question.getQuestion());
        holder.optionsGroup.removeAllViews();

        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(question.getOptions().get(i));
            int index = i;
            radioButton.setOnClickListener(v -> selectedAnswers[position] = index);
            holder.optionsGroup.addView(radioButton);
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioGroup optionsGroup;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            optionsGroup = itemView.findViewById(R.id.options_group);
        }
    }
}
