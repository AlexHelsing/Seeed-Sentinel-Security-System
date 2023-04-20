package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.andrognito.patternlockview.PatternLockView;

public class SettingsActivity extends AppCompatActivity {
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    AppCompatButton editProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        editProfileBtn = findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(view -> Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show());


        // return to dashboard
        backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> finish());


        // navigate to set pattern activity
        navigateToPatternBtn = findViewById(R.id.NavigateToSetPattern);
        navigateToPatternBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
            startActivity(intent);
        });

    }
}
