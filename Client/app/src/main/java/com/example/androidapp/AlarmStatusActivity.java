package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MQTT.MqttClient;
import com.example.androidapp.MQTT.MqttHandler;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttToken;


public class AlarmStatusActivity extends AppCompatActivity {

    // BUTTONS AND TEXT IN THE VIEW
    ImageView backButton;
    Button alarmStatusButton;
    Button deactivateActivateButton;
    TextView alarmStatusText;
    TextView hallwayStatus;
    TextView livingRoomStatus;

    // false = off
    public boolean alarmStatus = true;

    // MQTT BROKER
    private static final String BROKER_URL = "tcp://broker.hivemq.com";
    private static final String ALARM_TOPIC = "/SeeedSentinel/AlarmOnOff";
    private static final String CLIENT_ID = "SentinelApp";

    private BrokerConnection brokerConnection;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmstatus);



        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.connectToMqttBroker();

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(AlarmStatusActivity.this, MainActivity.class);
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
        });

        alarmStatusText = findViewById(R.id.alarmStatusText);
        hallwayStatus = findViewById(R.id.hallwayStatus);
        livingRoomStatus = findViewById(R.id.livingRoomStatus);

        deactivateActivateButton = findViewById(R.id.btn_deactivateActivateAlarm);


        alarmStatusButton = findViewById(R.id.btn_changeAlarmStatus);
        alarmStatusButton.setOnClickListener(view -> {
            alarmStatusButton.setText("Turn Off Alarm");
            alarmStatusButton.setBackgroundColor(Color.parseColor("#808080"));
        });

        deactivateActivateButton.setOnClickListener(view -> {
            if (deactivateActivateButton.getText().equals("Deactivate Alarm")) {
                deactivateActivateButton.setText("Activate Alarm");
                alarmStatusText.setText("The alarm is disarmed");
                alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
                hallwayStatus.setText("Alarm: Unarmed");
                hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
                livingRoomStatus.setText("Alarm: Unarmed");
                livingRoomStatus.setTextColor(Color.parseColor("#DDFF0000"));
            } else if (deactivateActivateButton.getText().equals("Activate Alarm")) {
                deactivateActivateButton.setText("Deactivate Alarm");
                alarmStatusText.setText("The alarm is armed");
                alarmStatusText.setTextColor(Color.parseColor("#DD59FF00"));
                hallwayStatus.setText("Alarm: Armed");
                hallwayStatus.setTextColor(Color.parseColor("#DD59FF00"));
                livingRoomStatus.setText("Alarm: Armed");
                livingRoomStatus.setTextColor(Color.parseColor("#DD59FF00"));
            }});

    }

}
