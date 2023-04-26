package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import org.bson.Document;

public class SettingsActivity extends AppCompatActivity {
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        if (app.currentUser() == null) {
            Toast.makeText(getApplicationContext(), "Please log in.", Toast.LENGTH_SHORT).show();
        }


        // fetch users name
         TextView unamefield = findViewById(R.id.user_name);


        // refresh custom data and update the UI
        app.currentUser().refreshCustomData(it -> {
            if (it.isSuccess()) {
                Log.v("SettingsActivity", "Successfully refreshed custom data.");
                Document customData = app.currentUser().getCustomData();
                Log.v("SettingsActivity", "Custom data: " + customData.toString());
                unamefield.setText(customData.getString("name"));
            } else {
                Log.v("SettingsActivity", "Failed to refresh custom data: " + it.getError().getErrorMessage());
            }
        });

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

    }
}
