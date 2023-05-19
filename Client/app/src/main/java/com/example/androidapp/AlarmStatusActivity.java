package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.w3c.dom.Text;

import com.example.androidapp.Settings.SettingsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;


public class AlarmStatusActivity extends AppCompatActivity {

    // BUTTONS AND TEXT IN THE VIEW
    ImageView backButton;
    Button deactivateActivateButton;
    TextView alarmStatusText;
    TextView hallwayStatus;
    TextView headerView;
    ScrollView scrollviewEdit;
    TextView wioLocationName;
    LinearLayout wioLocationCard;
    UserViewModel userViewModel;

    private BrokerConnection brokerConnection;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmstatus);

        dbHandler db = new dbHandler(getApplicationContext());
        userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);

        MyApp myApp = (MyApp) getApplication();
        brokerConnection = myApp.getBrokerConnection();


        userViewModel.getUser().observe(this, user -> {
            String passcode = user.getPasscode();
            String name = user.getName();
            // send the passcode and name to the broker, refactor in the future
            brokerConnection.publishMqttMessage("/SeeedSentinel/GetPasscodeFromClient", passcode, "");
            brokerConnection.publishMqttMessage("/SeeedSentinel/GetUserProfile", name, "");
        });


        // VIEW MODEL to get the alarm status
        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(AlarmStatusActivity.this, MainActivity.class);
            startActivity(intent);
        });

        alarmStatusText = findViewById(R.id.alarmStatusText);
        hallwayStatus = findViewById(R.id.hallwayStatus);
        scrollviewEdit = findViewById(R.id.bc_scrollview);
        headerView = findViewById(R.id.header_view);
        wioLocationName = findViewById(R.id.wioLocationName);


        deactivateActivateButton = findViewById(R.id.btn_deactivateActivateAlarm);
        deactivateActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(AlarmViewModel.getAlarmStatus().getValue(), "AlarmOff")){
                    alarmViewModel.setAlarmStatus("AlarmOn");
                    brokerConnection.publishMqttMessage("/SeeedSentinel/AlarmOnOff", "AlarmOn", "ChangeAlarmStatus");
                }
                else if(Objects.equals(AlarmViewModel.getAlarmStatus().getValue(), "AlarmOn")){
                    alarmViewModel.setAlarmStatus("AlarmOff");
                    brokerConnection.publishMqttMessage("/SeeedSentinel/AlarmOnOff","AlarmOff", "ChangeAlarmStatus");
                }
                else if(Objects.equals(AlarmViewModel.getAlarmStatus().getValue(), "AlarmIntruder")){
                    alarmViewModel.setAlarmStatus("AlarmOff");
                    brokerConnection.publishMqttMessage("/SeeedSentinel/AlarmOnOff", "AlarmOff", "ChangeAlarmStatus");
            }
        }});

        // Press the card to change name of wio location
        wioLocationCard = findViewById(R.id.wioCardName);
        wioLocationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWioName();
            }
        });

        // Observes name of current users wio terminal location
        userViewModel.getUser().observe(this, userModel -> {
            if(userModel.getWioLocation() == null || Objects.equals(userModel.getWioLocation(), "")){
                wioLocationName.setText("Seeed Sentinel - Press card to enter location");
            }
            else{
            wioLocationName.setText(userModel.getWioLocation());
        }}

        );

        // Observes global alarm status and changes UI depending on the status
        AlarmViewModel.getAlarmStatus().observe(this, alarm -> {
            switch (alarm) {
                case "AlarmOff":
                    activateAlarmUI();
                    break;
                case "AlarmIntruder":
                    intruderAlarmUI();
                    break;
                case "AlarmOn":
                    deactivateAlarmUI();
                    break;
            }
        });
    }

    // Three different UI states depending on alarm status
    public void activateAlarmUI(){
        deactivateActivateButton.setText("Activate Alarm");
        alarmStatusText.setText("The alarm is disarmed");
        alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
        headerView.setBackgroundResource(R.drawable.bluegradient);
        scrollviewEdit.setBackgroundResource(R.drawable.bluegradient);
        hallwayStatus.setText("Alarm: Unarmed");
        hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
    }

    public void deactivateAlarmUI(){
        deactivateActivateButton.setText("Deactivate Alarm");
        alarmStatusText.setText("The alarm is armed");
        scrollviewEdit.setBackgroundResource(R.drawable.bluegradient);
        alarmStatusText.setTextColor(Color.parseColor("#DD59FF00"));
        headerView.setBackgroundResource(R.drawable.bluegradient);
        hallwayStatus.setText("Alarm: Armed");
        hallwayStatus.setTextColor(Color.parseColor("#DD59FF00"));
    }

    public void intruderAlarmUI(){
        deactivateActivateButton.setText("Turn off alarm");
        scrollviewEdit.setBackgroundColor(Color.parseColor("#CF0107"));
        alarmStatusText.setText("Intruder Alert!");
        alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
        headerView.setBackgroundColor(Color.parseColor("#CF0107"));
        hallwayStatus.setText("Alarm: INTRUDER ALERT");
        hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
    }

    // Function for pop up window where user can enter a new location for wio terminal, in case they
    // change the position of it
    public void editWioName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmStatusActivity.this);
        builder.setTitle("Seeed Sentinel Name");
        builder.setMessage("Edit name");
        builder.setCancelable(true);
        // Input field for user
        final EditText input = new EditText(this);
        builder.setView(input);
        // Done and cancel button
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newLocation = String.valueOf(input.getText());
                if(newLocation.length() == 0 || newLocation.length() >= 25){
                    Toast.makeText(getApplicationContext(), "Please enter between 1-25 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                userViewModel.editWioLocation(newLocation);
                Toast.makeText(getApplicationContext(), "Seed Sentinel location name has been updated", Toast.LENGTH_SHORT).show();
            }
        }});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
