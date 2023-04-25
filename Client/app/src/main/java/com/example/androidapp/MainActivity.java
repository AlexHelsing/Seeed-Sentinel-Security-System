package com.example.androidapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;

    LinearLayout placeHolderbutton;

    private static final String BROKER_URL = "tcp://10.0.2.2:1883";

    private static final String CLIENT_ID = "SentinelApp";

    private MqttHandler mqttHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        placeHolderbutton = findViewById(R.id.placeholder_button);
        placeHolderbutton.setOnClickListener(view -> {
            // start history activity
            Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
            startActivity(intent);
        });
    }
}
