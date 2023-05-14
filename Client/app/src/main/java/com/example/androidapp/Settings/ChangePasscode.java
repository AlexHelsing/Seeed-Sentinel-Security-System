package com.example.androidapp.Settings;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.KeypadUtils;
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
        setButtonOnClickListener(one, 1);

        Button two = findViewById(R.id.button2);
        setButtonOnClickListener(two, 2);

        Button three = findViewById(R.id.button3);
        setButtonOnClickListener(three, 3);

        Button four = findViewById(R.id.button4);
        setButtonOnClickListener(four, 4);

        Button five = findViewById(R.id.button5);
        setButtonOnClickListener(five, 5);

        Button six = findViewById(R.id.button6);
        setButtonOnClickListener(six, 6);

        Button seven = findViewById(R.id.button7);
        setButtonOnClickListener(seven, 7);

        Button eight = findViewById(R.id.button8);
        setButtonOnClickListener(eight, 8);


        Button nine = findViewById(R.id.button9);
        setButtonOnClickListener(nine, 9);

        ImageButton delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(view -> {
            if (InputPasscode.size() > 0) {
                InputPasscode.remove(InputPasscode.size() - 1);
                KeypadUtils.updateUI(InputPasscode, inputState);
            }
        });



    }

    // so we dont have to write the same code for each button
    private void setButtonOnClickListener(Button button, final int number) {
        button.setOnClickListener(view -> {
            boolean valid = KeypadUtils.validateButton(number, InputPasscode);
            if (valid) {
                inputState.setText(String.valueOf(number));
                InputPasscode.add(number);
                KeypadUtils.updateUI(InputPasscode, inputState);
            }
        });
    }
}
