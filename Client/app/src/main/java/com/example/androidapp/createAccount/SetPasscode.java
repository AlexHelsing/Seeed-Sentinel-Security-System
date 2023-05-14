package com.example.androidapp.createAccount;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.androidapp.KeypadUtils;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UpdateUserDataCallback;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import java.util.ArrayList;
import java.util.List;


public class SetPasscode extends AppCompatActivity {

    dbHandler db;
    App app;
    User currentUser;

    Bundle extra;
    Button inputState;

    String name;
    String profilePic;
    Button submitButton;

    List<Integer> InputPasscode = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_setpasscode);
        // get the name from the previous activity
        extra = getIntent().getExtras();
        name = extra.getString("name");
        profilePic = extra.getString("profilePic");

        db = new dbHandler(getApplicationContext());
        app = db.getApp();
        currentUser = app.currentUser();

        inputState = findViewById(R.id.current_passcode);

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

        submitButton = findViewById(R.id.savePasscode);
        submitButton.setOnClickListener(view -> {
            String TempPasscodeString = "";
            if (InputPasscode.size() < 3) {
                Toast.makeText(this, "Gotta be atleast 3 numbers", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < InputPasscode.size(); i++) {
                    TempPasscodeString += InputPasscode.get(i);
                }
                dbHandler.setCustomData(currentUser, name, TempPasscodeString, profilePic, new UpdateUserDataCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(SetPasscode.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(SetPasscode.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
    }
    private void setButtonOnClickListener(Button button, final int number) {
        button.setOnClickListener(view -> {
            Log.d("Keypad", "Button " + number + " pressed");
            boolean valid = KeypadUtils.validateButton(number, InputPasscode);
            if (valid) {
                inputState.setText(String.valueOf(number));
                InputPasscode.add(number);
                KeypadUtils.updateUI(InputPasscode, inputState);
            }
        });
    }
}