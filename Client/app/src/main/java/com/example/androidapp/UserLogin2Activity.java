package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserLogin2Activity extends AppCompatActivity {

    Button continueButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login2);

        continueButton = findViewById(R.id.btn_continue2);
        continueButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(UserLogin2Activity.this, UserLoginPatternActivity.class);
            startActivity(intent);
        });
    }
}