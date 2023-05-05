package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


public class AlarmStatusActivity extends AppCompatActivity {

    // BUTTONS AND TEXT IN THE VIEW
    ImageView backButton;
    Button alarmStatusButton;
    Button deactivateActivateButton;
    TextView alarmStatusText;
    TextView hallwayStatus;
    TextView livingRoomStatus;

    private BrokerConnection brokerConnection;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmstatus);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.connectToMqttBroker();

        // VIEW MODEL to get the alarm status
        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

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

        System.out.println(alarmViewModel.getAlarmStatus().getValue());

        deactivateActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmViewModel.getAlarmStatus().getValue() == false){
                    alarmViewModel.setAlarmStatus(true);
                    deactivateActivateButton.setText("Deactivate Alarm");
                    alarmStatusText.setText("The alarm is armed");
                    alarmStatusText.setTextColor(Color.parseColor("#DD59FF00"));
                    hallwayStatus.setText("Alarm: Armed");
                    hallwayStatus.setTextColor(Color.parseColor("#DD59FF00"));
                    livingRoomStatus.setText("Alarm: Armed");
                    livingRoomStatus.setTextColor(Color.parseColor("#DD59FF00"));
                    brokerConnection.publishMqttMessage("AlarmOn", "ChangeAlarmStatus");
                }
                else if(alarmViewModel.getAlarmStatus().getValue() == true){
                    alarmViewModel.setAlarmStatus(false);
                    deactivateActivateButton.setText("Activate Alarm");
                    alarmStatusText.setText("The alarm is disarmed");
                    alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
                    hallwayStatus.setText("Alarm: Unarmed");
                    hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
                    livingRoomStatus.setText("Alarm: Unarmed");
                    livingRoomStatus.setTextColor(Color.parseColor("#DDFF0000"));
                    brokerConnection.publishMqttMessage("AlarmOff", "ChangeAlarmStatus");
                }
            }
        });

        alarmViewModel.getAlarmStatus().observe(this, alarm -> {
            if (alarm) {
                deactivateActivateButton.setText("Deactivate Alarm");
                alarmStatusText.setText("The alarm is armed");
                alarmStatusText.setTextColor(Color.parseColor("#DD59FF00"));
                hallwayStatus.setText("Alarm: Armed");
                hallwayStatus.setTextColor(Color.parseColor("#DD59FF00"));
                livingRoomStatus.setText("Alarm: Armed");
                livingRoomStatus.setTextColor(Color.parseColor("#DD59FF00"));
            } else {
                deactivateActivateButton.setText("Activate Alarm");
                alarmStatusText.setText("The alarm is disarmed");
                alarmStatusText.setTextColor(Color.parseColor("#DDFF0000"));
                hallwayStatus.setText("Alarm: Unarmed");
                hallwayStatus.setTextColor(Color.parseColor("#DDFF0000"));
                livingRoomStatus.setText("Alarm: Unarmed");
                livingRoomStatus.setTextColor(Color.parseColor("#DDFF0000"));
            }
        });
    }
}
