package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import com.example.androidapp.Settings.SettingsActivity;

import java.util.Objects;


public class AlarmStatusActivity extends AppCompatActivity {

    // BUTTONS AND TEXT IN THE VIEW
    ImageView backButton;
    Button deactivateActivateButton;
    TextView alarmStatusText;
    TextView hallwayStatus;
    TextView livingRoomStatus;

    TextView headerView;
    ScrollView scrollviewEdit;

    private BrokerConnection brokerConnection;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmstatus);

        dbHandler dbHandler = new dbHandler(getApplicationContext());

        MyApp myApp = (MyApp) getApplication();
        brokerConnection = myApp.getBrokerConnection();

        UserViewModel userViewModel = new UserViewModelFactory(dbHandler).create(UserViewModel.class);
       // get the passcode and name and then send it to the broker

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
        deactivateActivateButton = findViewById(R.id.btn_deactivateActivateAlarm);
        scrollviewEdit = findViewById(R.id.bc_scrollview);
        headerView = findViewById(R.id.header_view);



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
}
