package com.example.androidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.androidapp.History.HistoryActivity;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.Settings.SettingsActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import io.realm.mongodb.App;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "AlarmStatus";
    dbHandler db;
    App app;

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;
    LinearLayout placeHolderbutton;

    BrokerConnection brokerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        createNotificationChannel();


        // if user is not authed, send them to the starter page
        if (app.currentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), StarterPage.class);
            startActivity(intent);
            finish();
        }

        UserViewModel userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);


        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);


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

        // PLACEHOLDER BUTTON SETTINGS
        placeHolderbutton = findViewById(R.id.placeholder_button);
        placeHolderbutton.setOnClickListener(view -> {
            //userViewModel.getUser().getValue().getBreakins().forEach(breakin -> {
            //Log.v(breakin.get("location").toString(), breakin.get("date").toString());
        //});
         });

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlarmStatus";
            String description = "AlarmStatus";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("AlarmStatus", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
