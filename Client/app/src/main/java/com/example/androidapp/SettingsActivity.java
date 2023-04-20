package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        ImageView backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> finish());


        LinearLayout choose_patternButton = findViewById(R.id.layout1);

        choose_patternButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
            startActivity(intent);
        });


    }
}
