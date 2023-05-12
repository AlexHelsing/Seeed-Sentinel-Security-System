package com.example.androidapp;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity  extends AppCompatActivity {

    dbHandler db;
    App app;

    Button loginButton;
    EditText email;
    EditText password;
    ImageView returnButton;
    String emailText;
    String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivitypage);

        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.login);
        returnButton = findViewById(R.id.goback);

        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, StarterPage.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            emailText = email.getText().toString();
            passwordText = password.getText().toString();
            Credentials credentials = Credentials.emailPassword(emailText, passwordText);


            AtomicReference<User> user = new AtomicReference<User>();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    user.set(app.currentUser());
                    System.out.println("Successfully authenticated using an email and password.");
                    System.out.println("User: " + user.get().getId());

                    // Start the main activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    System.out.println("Error logging in: " + it.getError().getErrorMessage());
                }
            });

        });
    }
}
