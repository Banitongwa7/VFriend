package com.example.vfriend;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide toolbar
        getSupportActionBar().hide();

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcom_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            addToChat(message, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(message);
            welcomeTextView.setVisibility(View.GONE);
        });
    }

    void addToChat(String message, String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        //entrain d'ecrire le message
        messageList.remove(messageList.size()-1);

        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String message){
        //entrain d'ecrire le message
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        //okhttp
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model","text-davinci-003");
            jsonObject.put("prompt",message);
            jsonObject.put("max_tokens",4000);
            jsonObject.put("temperature",0);
        }catch (JSONException e){
            e.printStackTrace();
        }
        @Deprecated
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    addResponse("Erreur : " + response.body().string());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                addResponse("Erreur : " + e.getMessage());
            }
        });
    }
}