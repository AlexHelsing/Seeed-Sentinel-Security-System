package com.example.androidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import com.example.androidapp.History.HistoryActivity;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MQTT.MqttClient;
import com.example.androidapp.Settings.SettingsActivity;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import io.realm.mongodb.App;


public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "AlarmStatus";
    dbHandler db;
    App app;

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;
    LinearLayout placeHolderbutton;

    private MqttClient mqttClient;
    private BrokerConnection brokerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);

        createNotificationChannel();

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

        // PLACEHOLDER BUTTON SETTINGS
        placeHolderbutton = findViewById(R.id.placeholder_button);
        placeHolderbutton.setOnClickListener(view -> {
            //userViewModel.getUser().getValue().getBreakins().forEach(breakin -> {
            //Log.v(breakin.get("location").toString(), breakin.get("date").toString());
        //});
         });

    }

    private SettingsActivity settings = new SettingsActivity();

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

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int selectedPosition = sharedPreferences.getInt("notification_position", 0);
            switch (selectedPosition) {
                case 0:
                    // No notifications
                    channel.setImportance(NotificationManager.IMPORTANCE_NONE);
                    break;
                case 1:
                    // Notify every entry
                    channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    break;
                case 2:
                    // Notify if alarm is not off
                    channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
                    break;
                default:
                    break;
            }
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        //notifications
        /*String topic = "notification_change";
        String message;
        int position = settings;
        switch (position){
            case 0:
                message = "no_notifications";
                break;
            case 1:
                message = "notifications_every_entry";
                break;
            case 2:
                message = "notifiy_if_alarm_not_off";
                break;
            default:
                message = "";
        }
        if (!message.isEmpty()){
            brokerConnection.publishMqttMessage(topic, message);
        }

         */
    }
}

