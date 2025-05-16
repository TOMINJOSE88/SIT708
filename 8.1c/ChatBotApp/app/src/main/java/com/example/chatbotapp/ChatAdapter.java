package com.example.chatbotapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
            return new UserMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_message, parent, false);
            return new BotMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserMessageHolder) {
            ((UserMessageHolder) holder).textView.setText(message.getMessage());
        } else {
            ((BotMessageHolder) holder).textView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserMessageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        UserMessageHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.userMessageText);
        }
    }

    static class BotMessageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        BotMessageHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.botMessageText);
        }
    }
}
