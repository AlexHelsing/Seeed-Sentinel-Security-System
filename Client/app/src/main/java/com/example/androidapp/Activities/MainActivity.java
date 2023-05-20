package com.example.androidapp.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.ViewModels.AlarmViewModel;
import com.example.androidapp.R;
import com.example.androidapp.Settings.SettingsActivity;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;

import io.realm.mongodb.App;


public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "AlarmStatus";
    dbHandler db;
    App app;

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;
    private NotificationManagerCompat notificationManager;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);

        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(this);

        // if user is not authed, send them to the starter page
        if (app.currentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), StarterPage.class);
            startActivity(intent);
            finish();
        }


        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(MainActivity.this, AlarmStatusActivity.class);
            startActivity(intent);
        });

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            // start settings activity
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
        );

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> {
            // start history activity
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });

        userName = findViewById(R.id.userWelcome);
        userViewModel.getUser().observe(this , userModel -> {
            if(userModel.getName() == null || userModel.getName().equals("")){
                userName.setText("Welcome");
            }
            else {
                userName.setText("Welcome " + userModel.getName());
            }
        });

    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlarmStatus";
            CharSequence name2 = "Call the police";
            CharSequence name3 = "Call registered emergency contact";
            String description = "AlarmStatus";
            String description2 = "Call the police";
            String description3 = "Call registered emergency contact";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("AlarmStatus", name, importance);
            NotificationChannel channel2 = new NotificationChannel("CallPolice", name2, importance);
            NotificationChannel channel3 = new NotificationChannel("EmergencyContact", name3, importance);
            channel.setDescription(description);
            channel2.setDescription(description2);
            channel3.setDescription(description3);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
            notificationManager.createNotificationChannel(channel3);
        }
    }

}

