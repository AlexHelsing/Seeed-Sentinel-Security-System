package com.example.androidapp.Settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChangePasscode extends AppCompatActivity {


    ImageView backBtn;
    Button inputState;
    //array of ints to store the passcode
    List<Integer> passcode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_passcode_layout);



        backBtn = findViewById(R.id.back_button_setPasscode);
        backBtn.setOnClickListener(view -> {
            finish();
        });

        inputState = findViewById(R.id.current_passcode);
        inputState.setText("Enter Current Passcode");


        Button one = findViewById(R.id.button1);
        one.setOnClickListener(view -> {
            boolean valid = validateButton(1);
            if (valid) {
                inputState.setText("1");
                passcode.add(1);
                updateUI();
            }
        });

        Button two = findViewById(R.id.button2);
        two.setOnClickListener(view -> {
            boolean valid = validateButton(2);
            if (valid) {
                inputState.setText("2");
                passcode.add(2);
                updateUI();
            }
        });

        Button three = findViewById(R.id.button3);
        three.setOnClickListener(view -> {
            boolean valid = validateButton(3);
            if (valid) {
                inputState.setText("3");
                passcode.add(3);
                updateUI();
            }
        });

        Button four = findViewById(R.id.button4);
        four.setOnClickListener(view -> {
            boolean valid = validateButton(4);
            if (valid) {
                inputState.setText("4");
                passcode.add(4);
                updateUI();
            }

        });

        Button five = findViewById(R.id.button5);
        five.setOnClickListener(view -> {
            boolean valid = validateButton(5);
            if (valid) {
                inputState.setText("5");
                passcode.add(5);
                updateUI();
            }
        });

        Button six = findViewById(R.id.button6);
        six.setOnClickListener(view -> {
            boolean valid = validateButton(6);
            if (valid) {
                inputState.setText("6");
                passcode.add(6);
                updateUI();
            }
        });

        Button seven = findViewById(R.id.button7);
        seven.setOnClickListener(view -> {
            boolean valid = validateButton(7);
            if (valid) {
                inputState.setText("7");
                passcode.add(7);
                updateUI();
            }
        });

        Button eight = findViewById(R.id.button8);
        eight.setOnClickListener(view -> {
            boolean valid = validateButton(8);
            if (valid) {
                inputState.setText("8");
                passcode.add(8);
                updateUI();
            }
        });

        Button nine = findViewById(R.id.button9);
        nine.setOnClickListener(view -> {
            boolean valid = validateButton(9);
            if (valid) {
                inputState.setText("9");
                passcode.add(9);
                updateUI();
            }
        });

        ImageButton delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(view -> {
            if (passcode.size() > 0) {
                passcode.remove(passcode.size() - 1);
                updateUI();
            }
        });



    }

    // method to validate pressed button, we cant use a number more than once
    public boolean validateButton(int button) {
        if (passcode.contains(button)) {
            return false;
        } else {
            return true;
        }
    }

    // method to update the ui with all the numbers in the passcode array
    public void updateUI() {
        String passcodeString = "";
        for (int i = 0; i < passcode.size(); i++) {
            passcodeString += passcode.get(i);
        }
        inputState.setText(passcodeString);
    }
}
