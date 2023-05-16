package com.example.androidapp.Settings;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;

import java.util.ArrayList;
import java.util.List;

public class ChangePasscode extends AppCompatActivity {


    ImageView backBtn;
    Button inputState;
    //array of ints to store the passcode
    List<Integer> InputPasscode = new ArrayList<>();

    String passcode1;
    UserViewModel userViewModel;

    ArrayList<Button> buttonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_passcode_layout);

        dbHandler db = new dbHandler(getApplicationContext());
        userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);

        inputState = findViewById(R.id.current_passcode);

        userViewModel.getUser().observe(this, user -> {

            inputState.setText("Current Passcode: " + user.getPasscode());
        });

        backBtn = findViewById(R.id.back_button_setPasscode);
        backBtn.setOnClickListener(view -> {
            finish();
        });


        Button saveButton = findViewById(R.id.savePasscode);
        saveButton.setOnClickListener(view -> {
            String TempPasscodeString = "";
            if (InputPasscode.size() < 3) {
                Toast.makeText(getApplicationContext(), "Passcode must be at least 3 digits long", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < InputPasscode.size(); i++) {
                    TempPasscodeString += InputPasscode.get(i);
                }
                userViewModel.editPasscode(TempPasscodeString);
                Toast.makeText(getApplicationContext(), "Passcode Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        Button one = findViewById(R.id.button1);
        one.setOnClickListener(view -> {
            boolean valid = validateButton(1);
            if (valid) {
                inputState.setText("1");
                InputPasscode.add(1);
                buttonList.add(one);
                updateUI();
            }
        });

        Button two = findViewById(R.id.button2);
        two.setOnClickListener(view -> {
            boolean valid = validateButton(2);
            if (valid) {
                inputState.setText("2");
                InputPasscode.add(2);
                buttonList.add(two);
                updateUI();
            }
        });

        Button three = findViewById(R.id.button3);
        three.setOnClickListener(view -> {
            boolean valid = validateButton(3);
            if (valid) {
                inputState.setText("3");
                InputPasscode.add(3);
                buttonList.add(three);
                updateUI();
            }
        });

        Button four = findViewById(R.id.button4);
        four.setOnClickListener(view -> {
            boolean valid = validateButton(4);
            if (valid) {
                inputState.setText("4");
                InputPasscode.add(4);
                buttonList.add(four);
                updateUI();
            }

        });

        Button five = findViewById(R.id.button5);
        five.setOnClickListener(view -> {
            boolean valid = validateButton(5);
            if (valid) {
                inputState.setText("5");
                InputPasscode.add(5);
                buttonList.add(five);
                updateUI();
            }
        });

        Button six = findViewById(R.id.button6);
        six.setOnClickListener(view -> {
            boolean valid = validateButton(6);
            if (valid) {
                inputState.setText("6");
                InputPasscode.add(6);
                buttonList.add(six);
                updateUI();
            }
        });

        Button seven = findViewById(R.id.button7);
        seven.setOnClickListener(view -> {
            boolean valid = validateButton(7);
            if (valid) {
                inputState.setText("7");
                InputPasscode.add(7);
                buttonList.add(seven);
                updateUI();
            }
        });

        Button eight = findViewById(R.id.button8);
        eight.setOnClickListener(view -> {
            boolean valid = validateButton(8);
            if (valid) {
                inputState.setText("8");
                InputPasscode.add(8);
                buttonList.add(eight);
                updateUI();
            }
        });

        Button nine = findViewById(R.id.button9);
        nine.setOnClickListener(view -> {
            boolean valid = validateButton(9);
            if (valid) {
                inputState.setText("9");
                InputPasscode.add(9);
                buttonList.add(nine);
                updateUI();
            }
        });

        ImageButton delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(view -> {
            if (InputPasscode.size() > 0) {
                buttonList.get(buttonList.size()-1).setBackgroundColor(Color.parseColor("#3b5998"));
                buttonList.remove(buttonList.size()-1);
                InputPasscode.remove(InputPasscode.size() - 1);
                updateUI();
            }
        });



    }

    // method to validate pressed button, we cant use a number more than once
    public boolean validateButton(int button) {
        if (InputPasscode.contains(button)) {
            return false;
        } else {
            return true;
        }
    }

    // method to update the ui with all the numbers in the passcode array
    public void updateUI() {
        String passcodeString = "";
        for (int i = 0; i < InputPasscode.size(); i++) {
            passcodeString += InputPasscode.get(i);
        }
        for(int i = 0; i < buttonList.size(); i++){
            buttonList.get(i).setBackgroundColor(Color.parseColor("#696969"));
        }

        inputState.setText(passcodeString);
    }

}
