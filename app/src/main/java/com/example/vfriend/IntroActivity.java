package com.example.vfriend;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // hide toolbar
        getSupportActionBar().hide();

        // next activity, after 5 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }, 5000);
    }
}