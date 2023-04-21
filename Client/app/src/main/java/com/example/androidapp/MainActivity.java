package com.example.androidapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView homeButton;
    ImageView settingsButton;
    ImageView historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Home pressed", Toast.LENGTH_SHORT).show());

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            // start settings activity
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);  // start settings activity
                }
        );

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "History pressed", Toast.LENGTH_SHORT).show());
    }
}
