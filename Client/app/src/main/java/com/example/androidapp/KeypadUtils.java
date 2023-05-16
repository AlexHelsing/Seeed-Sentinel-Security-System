package com.example.androidapp;

import android.widget.TextView;

import java.util.List;

public class KeypadUtils {


    public static boolean validateButton(int button, List<Integer> InputPasscode) {
        if (InputPasscode.contains(button)) {
            return false;
        } else {
            return true;
        }
    }

    // method to update the ui with all the numbers in the passcode array
    public static void updateUI(List<Integer> InputPasscode, TextView inputState) {
        String passcodeString = "";
        for (int i = 0; i < InputPasscode.size(); i++) {
            passcodeString += InputPasscode.get(i);
        }

        inputState.setText(passcodeString);
    }
}
