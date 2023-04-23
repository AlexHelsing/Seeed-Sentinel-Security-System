package com.example.androidapp;

import android.hardware.SensorEvent;

import java.util.LinkedList;

public class SensorTimestamps {
    //class is not finished yet. its just a skeleton for the moment, it has fake data instead of real.
//will be finished once we have finished mqtt topic publish.
    private LinkedList<String> timestamps = new LinkedList<>();

    public void FakeTimestampList() {
        timestamps.add("2023-04-22 12:34:56");
        timestamps.add("2023-04-22 12:35:02");
        timestamps.add("2023-04-22 12:36:22");
        timestamps.add("2023-04-22 12:37:56");
        timestamps.add("2023-04-22 12:43:02");
        timestamps.add("2023-04-22 12:48:22");
        timestamps.add("2023-04-22 12:50:56");
        timestamps.add("2023-04-22 12:51:02");
        timestamps.add("2023-04-22 12:52:22");
        timestamps.add("2023-04-22 12:53:22");

        if (timestamps.size() > 10) {
            timestamps.removeFirst();
        }
    }

    public LinkedList<String> getTimestamps(){
        return timestamps;
    }
}
