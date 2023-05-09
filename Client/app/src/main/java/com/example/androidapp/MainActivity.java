package com.example.androidapp;

import android.content.Intent;
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

import io.realm.mongodb.App;

public class MainActivity extends AppCompatActivity {

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

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.connectToMqttBroker();

        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);


        // if user is not authed, send them to the starter page
        if (app.currentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), StarterPage.class);
            startActivity(intent);
        }


        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(MainActivity.this, AlarmStatusActivity.class);
            startActivity(intent);
            brokerConnection.getMqttClient().disconnect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Disconnected successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Disconnect failed, still connected");

                }
            });
            startActivity(intent);
        });

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            // start settings activity
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
            brokerConnection.getMqttClient().disconnect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Disconnected successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Disconnect failed, still connected");

                }
            });
                }
        );

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> {
            // start history activity
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            brokerConnection.getMqttClient().disconnect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Disconnected successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Disconnect failed, still connected");

                }
            });
            startActivity(intent);
        });

        // PLACEHOLDER BUTTON SETTINGS
        placeHolderbutton = findViewById(R.id.placeholder_button);
        placeHolderbutton.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Placeholder button", Toast.LENGTH_SHORT).show();
        });
    }
}
