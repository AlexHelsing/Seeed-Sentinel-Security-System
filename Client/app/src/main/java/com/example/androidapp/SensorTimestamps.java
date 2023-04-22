package com.example.androidapp;

import android.hardware.SensorEvent;

import java.util.LinkedList;

public class SensorTimestamps {
    //class is not finished yet. its just a skeleton for the moment.
//will be finished once we have finished mqtt topic publish.
    private LinkedList<String> timestamps = new LinkedList<>();

    public void sensorReactions(SensorEvent event){
        //data not available yet

        String timestamp = String.valueOf(System.currentTimeMillis());
        timestamps.add(timestamp);

        if (timestamps.size() > 10){
            timestamps.removeFirst();
        }

    }

    public LinkedList<String> getTimestamps(){
        return timestamps;
    }
}
