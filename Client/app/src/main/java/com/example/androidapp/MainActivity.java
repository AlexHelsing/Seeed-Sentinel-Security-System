package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(MainActivity.this, AlarmStatusActivity.class);
                                              startActivity(intent);
                                          }
                                      });

                // SETTINGS BUTTON SETTINGS
                settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Settings pressed", Toast.LENGTH_SHORT).show());

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "History pressed", Toast.LENGTH_SHORT).show());
    }
}
