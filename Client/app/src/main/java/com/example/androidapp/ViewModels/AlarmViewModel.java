package com.example.androidapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private static MutableLiveData<String> alarmStatus = new MutableLiveData<>("AlarmOff");



    public void setAlarmStatus(String status) {
        alarmStatus.setValue(status);
    }


    public static MutableLiveData<String> getAlarmStatus() {
        return alarmStatus;
    }
}
