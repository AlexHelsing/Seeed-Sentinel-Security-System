package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.andrognito.patternlockview.PatternLockView;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import org.bson.Document;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.androidapp.MQTT.MqttHandler;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    LinearLayout editSettingsBtn;
    private static final String BROKER_URL = "tcp://10.0.2.2:1883";
    private static final String CLIENT_ID = "SentinelApp";
    private MqttHandler mqttHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        if (app.currentUser() == null) {
            Toast.makeText(getApplicationContext(), "Please log in.", Toast.LENGTH_SHORT).show();
        }


        // name view
         TextView namefield = findViewById(R.id.user_name);


        // refresh custom data and update the UI // i dont think we have to do this every time but can fix later.
        app.currentUser().refreshCustomData(it -> {
            if (it.isSuccess()) {
                Log.v("SettingsActivity", "Successfully refreshed custom data.");
                Document customData = app.currentUser().getCustomData();
                Log.v("SettingsActivity", "Custom data: " + customData.toString());
                namefield.setText(customData.getString("name"));
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

        Spinner spinnerNotifications=findViewById(R.id.spinner_notifications);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.notifications, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotifications.setAdapter(adapter);
        spinnerNotifications.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        String topic = "notification_change"; // Replace with your desired topic

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        String message;
        switch (position) {
            case 0:
                message = "no_notifications";
                break;
            case 1:
                message = "notify_every_entry";
                break;
            case 2:
                message = "notify_if_alarm_not_off";
                break;
            default:
                message = "";
        }

        if (!message.isEmpty()) {
            mqttHandler.publish(topic, message);
        }
    }

    public void publishMessage(String topic, String message){

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
