package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.andrognito.patternlockview.PatternLockView;
import io.realm.Realm;
import io.realm.mongodb.App;
import org.bson.Document;
import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    AppCompatButton editProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        // refresh custom data
       // app.currentUser().refreshCustomData(it -> {
         //   if (it.isSuccess()) {
               // Log.v("SettingsActivity", "Successfully refreshed custom data.");
           // } else {
             //   Log.v("SettingsActivity", "Failed to refresh custom data: " + it.getError().getErrorMessage());
           // }
        //});


        Document n = app.currentUser().getCustomData();

        Log.v("SettingsActivity", "Custom data: " + n.get("name"));





        TextView unamefield = findViewById(R.id.user_name);
        unamefield.setText(n.get("name").toString());




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
