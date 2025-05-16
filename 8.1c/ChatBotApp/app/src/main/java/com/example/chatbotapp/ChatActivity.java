package com.example.chatbotapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText editMessage;
    private Button btnSend;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private final List<ChatMessage> chatMessages = new ArrayList<>();
    private String username;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        username = getIntent().getStringExtra("username");
        queue = Volley.newRequestQueue(this);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessage(message, ChatMessage.TYPE_USER);
                sendToBackend(message);
                editMessage.setText("");
            }
        });
    }

    private void addMessage(String message, int type) {
        chatMessages.add(new ChatMessage(message, type));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    private void sendToBackend(String userMessage) {
        String url = "http://192.168.0.62:5000/chat";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> addMessage(response, ChatMessage.TYPE_BOT),
                error -> addMessage("Bot: Error - " + error.toString(), ChatMessage.TYPE_BOT)) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userMessage", userMessage);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,  // 30 seconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(postRequest);
    }
}
