package com.example.androidapp.createAccount;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.androidapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SetName extends AppCompatActivity {

    Button continueButton;
    TextInputEditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_setname);

        nameField = findViewById(R.id.textInputEditText);

        continueButton = findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(view -> {
            // store namefield value as extra and pass to next activity
            Intent intent = new Intent(SetName.this, SetProfilePic.class);
            intent.putExtra("name", nameField.getText().toString());
            startActivity(intent);
        });





    }
}