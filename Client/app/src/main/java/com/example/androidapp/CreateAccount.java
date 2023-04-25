package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        if (app.currentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), setNamePage.class);
            startActivity(intent);
        }

        EditText email = findViewById(R.id.ca_email);
        EditText password = findViewById(R.id.ca_password);

        Button createAccountButton = findViewById(R.id.create_account_button);

        createAccountButton.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            Log.v("CreateAccount", "email: " + emailText + " password: " + passwordText);

            app.getEmailPassword().registerUserAsync(emailText, passwordText, it -> {
                if (it.isSuccess()) {
                    Log.v("CreateAccount", "Successfully registered user.");

                    Credentials credentials = Credentials.emailPassword(emailText, passwordText);
                    app.loginAsync(credentials, res -> {
                        if (res.isSuccess()) {
                            Log.v("CreateAccount", "Successfully logged in.");
                            // not sure where we should send users after creating account yet.
                            Intent intent = new Intent(getApplicationContext(), setNamePage.class);
                            startActivity(intent);
                        } else {
                            Log.v("CreateAccount", "Failed to log in: " + res.getError().getErrorMessage());
                            Toast.makeText(getApplicationContext(), "Failed to log in: " + res.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.v("CreateAccount", "Failed to register user: " + it.getError().getErrorMessage());
                    Toast.makeText(getApplicationContext(), "Failed to register user: " + it.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
