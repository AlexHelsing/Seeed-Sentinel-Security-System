package com.example.androidapp.History;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TableLayout;

import com.example.androidapp.AlarmViewModel;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MainActivity;
import com.example.androidapp.MyApp;
import com.example.androidapp.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TableRow tableRow;
    Button backButton;

    BrokerConnection brokerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        MyApp myApp = (MyApp) getApplication();
        brokerConnection = myApp.getBrokerConnection();

        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addTableRows(TableLayout tableLayout) {
        tableLayout = findViewById(R.id.tableLayout);
        SensorTimestamps sensorTimestamps = new SensorTimestamps();
        LinkedList<String> timestamps = sensorTimestamps.getTimestamps();
        for (int i = 0; i < 10; i++) {
            tableRow = new TableRow(this);
            textView1 = new TextView(this);
            textView2 = new TextView(this);
            textView1.setText("Alarm History");
            textView2.setText(TextUtils.join("\n", timestamps));
            tableRow.addView(textView1);
            tableRow.addView(textView2);
            tableLayout.addView(tableRow);
        }
    }
}
