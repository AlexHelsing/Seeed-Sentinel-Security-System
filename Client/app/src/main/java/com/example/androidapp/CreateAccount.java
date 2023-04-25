package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.mongodb.App;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        EditText email = findViewById(R.id.ca_email);
        EditText password = findViewById(R.id.ca_password);

        Button createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            Log.v("CreateAccount", "email: " + emailText + " password: " + passwordText);
        });
    }
}
