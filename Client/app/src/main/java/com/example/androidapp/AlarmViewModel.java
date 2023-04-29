package com.example.androidapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private static MutableLiveData<Boolean> alarmStatus;

    public AlarmViewModel() {
        alarmStatus = new MutableLiveData<>();
        alarmStatus.setValue(false);
    }

    public void setAlarmStatus(boolean status) {
        alarmStatus.setValue(status);
    }


    public MutableLiveData<Boolean> getAlarmStatus() {
        return alarmStatus;
    }
}
