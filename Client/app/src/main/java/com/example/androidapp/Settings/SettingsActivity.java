package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.AlarmStatusActivity;
import com.example.androidapp.AlarmViewModel;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class SettingsActivity extends AppCompatActivity {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;

    BrokerConnection brokerConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.connectToMqttBroker();

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

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
        backArrow.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
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
