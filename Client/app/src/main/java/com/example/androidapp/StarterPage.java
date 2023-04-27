package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.createAccount.CreateAccountActivity;

public class StarterPage extends AppCompatActivity{
    TextView signIn;
    TextView createAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter_page_createaccount_loginaccount);

        signIn = findViewById(R.id.btn_signin);
        signIn.setOnClickListener(view -> {
            // Start the login activity
            Intent intent = new Intent(StarterPage.this, LoginActivity.class);
            startActivity(intent);
        });

        createAccount = findViewById(R.id.btn_createaccount);
        createAccount.setOnClickListener(view -> {
            // start create account activity
            Intent intent = new Intent(StarterPage.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}
