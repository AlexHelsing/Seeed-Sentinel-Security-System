package com.example.androidapp;

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

        // I changed all "onClick" functions to lambda functions to keep code concise

        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Home pressed", Toast.LENGTH_SHORT).show());

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Settings pressed", Toast.LENGTH_SHORT).show());

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "History pressed", Toast.LENGTH_SHORT).show());
    }
}
