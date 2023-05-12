package com.example.androidapp.Settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MQTT.MqttClient;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.dbHandler;

import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.squareup.picasso.Picasso;
import io.realm.mongodb.App;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    private static final String CHANNEL_ID = "AlarmStatus";
    public static final String CHANNEL_ID2 = "AlarmStatusSilent";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);
        createNotificationChannels();

        TextView username = findViewById(R.id.user_name);
        ImageView profilePic = findViewById(R.id.profilePicture);


        userViewModel.getUser().observe(this, userModel -> {
                    username.setText(userModel.getName());
                    Picasso.get().load(userModel.getProfileImg()).into(profilePic);
                }

        );



        LogOutButton = findViewById(R.id.LogOutButton);
        LogOutButton.setOnClickListener(view -> {
            app.currentUser().logOutAsync(result -> {

                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.");
                    // clear the viewmodel data
                    Intent intent = new Intent(getApplicationContext(), StarterPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("AUTH", "Failed to log out, error: " + result.getError().getErrorMessage());
                }
            });
        });


        // edit profile button
        editProfileBtn = findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // open a dialog when user clicks on edit profile button

        // navigate to change passcode activity
        navigateToPasscodeBtn = findViewById(R.id.navigateToSetKeyword);
        navigateToPasscodeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasscode.class);
            startActivity(intent);
        });


        // return to dashboard
        backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> {
            // start alarm status activity
            finish();
        });


        // navigate to set pattern activity
        navigateToPatternBtn = findViewById(R.id.NavigateToSetPattern);
        navigateToPatternBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePattern.class);
            startActivity(intent);
        });

        Spinner spinnerNotifications=findViewById(R.id.spinner_notifications);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.notifications, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotifications.setAdapter(adapter);
        spinnerNotifications.setOnItemSelectedListener(this);

    }

    private void createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel AlarmStatus = new NotificationChannel(CHANNEL_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            AlarmStatus.setDescription("Alarm activated");

            NotificationChannel AlarmStatusSilent = new NotificationChannel(CHANNEL_ID2, "Channel 2", NotificationManager.IMPORTANCE_NONE);
            AlarmStatusSilent.setDescription("You should not get this notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(AlarmStatus);
            manager.createNotificationChannel(AlarmStatusSilent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //String item = adapterView.getItemAtPosition(position).toString();
        //Toast.makeText(SettingsActivity.this, "Selected Item: " + item, Toast.LENGTH_SHORT).show();

        AdapterView<?> Spinner = null;
        View TextView = null;
        if (onItemSelected(Spinner, TextView, 1, "item")){
            
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
    ArrayList<String> arrayList = new ArrayList<>();
}
