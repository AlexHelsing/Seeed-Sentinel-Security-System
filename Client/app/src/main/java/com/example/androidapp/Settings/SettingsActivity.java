package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.androidapp.*;
import io.realm.mongodb.App;


public class SettingsActivity extends AppCompatActivity {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        db = new dbHandler(getApplicationContext());
        UserViewModel userViewModel = new UserViewModel(db);

        TextView username = findViewById(R.id.user_name);

        userViewModel.getUser().observe(this, userModel -> {
            Log.v("AUTH", "User name: " + userModel.getName());
            username.setText(userModel.getName());
        });


        userViewModel.editName("pls work now");



        LogOutButton = findViewById(R.id.LogOutButton);
        LogOutButton.setOnClickListener(view -> {
            app.currentUser().logOutAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.");
                    Intent intent = new Intent(getApplicationContext(), StarterPage.class);
                    startActivity(intent);
                } else {
                    Log.e("AUTH", "Failed to log out, error: " + result.getError().getErrorMessage());
                }
            });
        });


        // edit profile button
        editProfileBtn = findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(view -> Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show());




        // return to dashboard
        backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> finish());


        // navigate to set pattern activity
        navigateToPatternBtn = findViewById(R.id.NavigateToSetPattern);
        navigateToPatternBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePattern.class);
            startActivity(intent);
        });

        // navigate to change passcode activity
        navigateToPasscodeBtn = findViewById(R.id.navigateToSetKeyword);
        navigateToPasscodeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasscode.class);
            startActivity(intent);
        });

    }
}
