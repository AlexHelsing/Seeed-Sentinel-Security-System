package com.example.androidapp.createAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.androidapp.R;

public class SetProfilePic extends AppCompatActivity {

    Button continueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_setpic);

        // get the name from the previous activity
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");

        continueButton = findViewById(R.id.btn_continue2);
        continueButton.setOnClickListener(view -> {
            // send the name to the next activity
            Intent intent = new Intent(SetProfilePic.this, SetPattern.class);
            intent.putExtra("name", name);
            startActivity(intent);
        });
    }
}