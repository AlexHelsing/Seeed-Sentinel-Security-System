package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserLoginPatternActivity extends AppCompatActivity {

    Button finishButton;

    // PLACEHOLDER BELOW, FIX PATTERN THINGY
    Button savePattern;

    // Why can't it find the ID? Check later
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_pattern);

        finishButton = findViewById(R.id.btn_finish);
        finishButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(UserLoginPatternActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}