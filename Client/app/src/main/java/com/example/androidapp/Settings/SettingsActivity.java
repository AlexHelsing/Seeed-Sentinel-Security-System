package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MQTT.MqttClient;
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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    LinearLayout editSettingsBtn;
    private static final String BROKER_URL = "tcp://10.0.2.2:1883";
    private static final String CLIENT_ID = "SentinelApp";
    private BrokerConnection brokerConnection;

    private MqttClient mqttClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        String topic = "notification_change"; // Replace with your desired topic

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
            brokerConnection.publishMqttMessage(topic, message);
        }
    }

    public void publishMessage(String topic, String message){

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // navigate to change passcode activity
        navigateToPasscodeBtn = findViewById(R.id.navigateToSetKeyword);
        navigateToPasscodeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasscode.class);
            startActivity(intent);
        });

    }
}
