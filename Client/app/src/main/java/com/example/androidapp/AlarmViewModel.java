package com.example.androidapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private static MutableLiveData<String> alarmStatus;

    public AlarmViewModel() {
        alarmStatus = new MutableLiveData<>();
        alarmStatus.setValue("AlarmOff");
    }

    public void setAlarmStatus(String status) {
        alarmStatus.setValue(status);
    }


    public MutableLiveData<String> getAlarmStatus() {
        return alarmStatus;
    }
}
