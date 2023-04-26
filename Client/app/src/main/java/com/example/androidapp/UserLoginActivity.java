package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserLoginActivity extends AppCompatActivity {

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        continueButton = findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(UserLoginActivity.this, UserLogin2Activity.class);
            startActivity(intent);
        });


    }
}