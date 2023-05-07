package com.example.androidapp.createAccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class CreateAccountActivity extends AppCompatActivity {

    dbHandler db;
    App app;
    Button createAccountButton;
    ImageView goBackButton;
    EditText email;
    EditText password;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        db = new dbHandler(getApplicationContext());
        app = db.getApp();


        if (app.currentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), StarterPage.class);
            startActivity(intent);
        }

        goBackButton = findViewById(R.id.goback);
        goBackButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, StarterPage.class);
            startActivity(intent);
        });

        email = findViewById(R.id.ca_email);
        password = findViewById(R.id.ca_password);

        createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            Log.v("CreateAccount", "email: " + emailText + " password: " + passwordText);

            // register the user
            app.getEmailPassword().registerUserAsync(emailText, passwordText, it -> {
                if (it.isSuccess()) {
                    Log.v("CreateAccount", "Successfully registered user.");

                    // login the user
                    Credentials credentials = Credentials.emailPassword(emailText, passwordText);
                    app.loginAsync(credentials, res -> {
                        if (res.isSuccess()) {
                            Log.v("CreateAccount", "Successfully logged in.");
                            // start onboarding process
                            Intent intent = new Intent(getApplicationContext(), SetName.class);
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
