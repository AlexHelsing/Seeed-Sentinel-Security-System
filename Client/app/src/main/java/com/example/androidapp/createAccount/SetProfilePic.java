package com.example.androidapp.createAccount;

import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.androidapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SetProfilePic extends AppCompatActivity {

    Button continueButton;

    Bundle extras;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_setpic);




        // get the name from the previous activity
        extras = getIntent().getExtras();
        name = extras.getString("name");

        continueButton = findViewById(R.id.btn_continue2);
        continueButton.setOnClickListener(view -> {
            // send the name to the next activity
            TextInputEditText profilePic = findViewById(R.id.profilePictureInput);
            String url = profilePic.getText().toString();

            if (url.isEmpty()) {
                Toast.makeText(SetProfilePic.this, "Please enter a profile picture URL", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(SetProfilePic.this, SetPasscode.class);
            intent.putExtra("name", name);
            intent.putExtra("profilePic", url);
            startActivity(intent);
        });

        TextView skipButton = findViewById(R.id.skipButton);
        skipButton.setOnClickListener(view -> {
            Intent intent = new Intent(SetProfilePic.this, SetPasscode.class);
            intent.putExtra("name", name);
            intent.putExtra("profilePic", "https://pbs.twimg.com/media/Cu-gvzDW8AEdu0o.jpg");
            startActivity(intent);
        });
    }
}