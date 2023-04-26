package com.example.androidapp;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import io.realm.mongodb.App;

public class MainActivity extends AppCompatActivity {

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        if (app.currentUser() == null) {
            Toast.makeText(getApplicationContext(), "Please log in.", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(getApplicationContext(), "Logged in as: " + app.currentUser().getId(), Toast.LENGTH_SHORT).show();

        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(MainActivity.this, AlarmStatusActivity.class);
            startActivity(intent);
        });

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            // start settings activity
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
        );

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> {
            // start history activity
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });
    }
}
