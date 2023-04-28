package com.example.androidapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;

public class AlarmStatusActivity extends AppCompatActivity {

    // BUTTONS AND TEXT IN THE VIEW
    ImageView backButton;
    Button alarmStatusButton;
    Button deactivateActivateButton;
    TextView alarmStatusText;
    TextView hallwayStatus;
    TextView livingRoomStatus;

    // false = off
    boolean alarmStatus = true;

    // MQTT BROKER
    private static final String BROKER_URL = "tcp://broker.hivemq.com";

    private static final String ALARM_TOPIC = "/SeeedSentinel/AlarmOnOff";
    private static final String CLIENT_ID = "SentinelApp";
    MqttHandler mqttHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmstatus);

        // Start and connect to mqtt
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttHandler.subscribe("/SeeedSentinel/AlarmOnOff");



        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(AlarmStatusActivity.this, MainActivity.class);
            startActivity(intent);
        });

        alarmStatusText = findViewById(R.id.alarmStatusText);
        hallwayStatus = findViewById(R.id.hallwayStatus);
        livingRoomStatus = findViewById(R.id.livingRoomStatus);

        deactivateActivateButton = findViewById(R.id.btn_deactivateActivateAlarm);
        deactivateActivateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (deactivateActivateButton.getText().equals("Deactivate Alarm")) {
                    deactivateActivateButton.setText("Activate Alarm");
                    alarmStatusText.setText("The alarm is disarmed");
                    alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
                    hallwayStatus.setText("Alarm: Unarmed");
                    hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
                    livingRoomStatus.setText("Alarm: Unarmed");
                    livingRoomStatus.setTextColor(Color.parseColor("#DDFF0000"));

                    mqttHandler.publish(ALARM_TOPIC, "DeactivateAlarm");
                } else if (deactivateActivateButton.getText().equals("Activate Alarm")) {
                    deactivateActivateButton.setText("Deactivate Alarm");
                    alarmStatusText.setText("The alarm is armed");
                    alarmStatusText.setTextColor(Color.parseColor("#DD59FF00"));
                    hallwayStatus.setText("Alarm: Armed");
                    hallwayStatus.setTextColor(Color.parseColor("#DD59FF00"));
                    livingRoomStatus.setText("Alarm: Armed");
                    livingRoomStatus.setTextColor(Color.parseColor("#DD59FF00"));
                    mqttHandler.publish(ALARM_TOPIC, "ActivateAlarm");
                }
            }
        });

        alarmStatusButton = findViewById(R.id.btn_changeAlarmStatus);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                while (alarmStatus) {
                    alarmStatusButton.setText("Turn Off Alarm");
                    alarmStatusButton.setBackgroundColor(Color.parseColor("#DDFF0000"));
                    mqttHandler.publish(ALARM_TOPIC, "TurnOffAlarm");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        Thread myThread = new Thread(myRunnable);
        myThread.start();
        alarmStatusButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                alarmStatusButton.setText("Turn Off Alarm");
                alarmStatusButton.setBackgroundColor(Color.parseColor("#808080"));
                mqttHandler.publish(ALARM_TOPIC, "TurnOffAlarm");
                alarmStatus = false;
            }
        });
    }}
