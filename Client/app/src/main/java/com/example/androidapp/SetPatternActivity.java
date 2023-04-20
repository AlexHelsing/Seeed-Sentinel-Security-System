package com.example.androidapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SetPatternActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpattern_layout);

        ImageView backArrow = findViewById(R.id.back_button2);

        backArrow.setOnClickListener(view -> finish());

    }


}
