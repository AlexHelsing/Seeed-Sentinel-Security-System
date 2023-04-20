package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.andrognito.patternlockview.PatternLockView;

public class SettingsActivity extends AppCompatActivity {
    ImageView backArrow;
    LinearLayout choose_patternButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        // return to dashboard
        backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> finish());


        // navigate to set pattern activity
        choose_patternButton = findViewById(R.id.NavigateToSetPattern);
        choose_patternButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
            startActivity(intent);
        });

    }
}
