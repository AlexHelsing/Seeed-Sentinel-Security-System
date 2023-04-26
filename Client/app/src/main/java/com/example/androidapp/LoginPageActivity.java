package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginPageActivity extends AppCompatActivity {

    TextView signIn;

    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        signIn = findViewById(R.id.btn_signin);
        signIn.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
            startActivity(intent);
        });

        createAccount = findViewById(R.id.btn_createaccount);
        createAccount.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}