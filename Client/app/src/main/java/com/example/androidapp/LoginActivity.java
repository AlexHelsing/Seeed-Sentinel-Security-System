package com.example.androidapp;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivitypage);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();


        EditText email = findViewById(R.id.Email);
        EditText password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            Credentials emailpassword = Credentials.emailPassword(emailText, passwordText);

            AtomicReference<User> user = new AtomicReference<User>();
            app.loginAsync(emailpassword, it -> {
                if (it.isSuccess()) {
                    user.set(app.currentUser());
                    System.out.println("Successfully authenticated using an email and password.");
                    System.out.println("User: " + user.get().getId());
                    // start dashboard activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    System.out.println("Error logging in: " + it.getError().getErrorMessage());
                }
            });

        });
    }
}
