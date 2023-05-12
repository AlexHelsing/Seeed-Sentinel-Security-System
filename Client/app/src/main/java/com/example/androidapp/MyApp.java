package com.example.androidapp;


import android.app.Application;

import com.example.androidapp.MQTT.BrokerConnection;

public class MyApp extends Application {

    private BrokerConnection brokerConnection;

    public void onCreate(){
        super.onCreate();
        brokerConnection = new BrokerConnection(this);
        brokerConnection.connectToMqttBroker();
    }

    public BrokerConnection getBrokerConnection() {
        return brokerConnection;
    }
}
