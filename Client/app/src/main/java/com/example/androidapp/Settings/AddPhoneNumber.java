package com.example.androidapp.Settings;

import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.ViewModels.UserViewModel;

/*public class AddPhoneNumber extends AppCompatActivity {
    UserViewModel userViewModel;
    //AddPhoneNumber.addPhone
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);

        // Initialize the UserViewModel
        dbHandler db = new dbHandler(getApplicationContext());
        userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);

        textButton = findViewById(R.id.textInputPhoneNumber);
        textButton2 = findViewById(R.id.textInputSecondPhoneNumber);
        textButton3 = findViewById(R.id.textInputThirdPhoneNumber);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoneNumber();
            }
        });

        textButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoneNumber();
            }
        });

        textButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoneNumber();
            }
        });

        // Observes name of current users wio terminal location
        userViewModel.getUser().observe(this, userModel -> {
            if (userViewModel.getUser() == null || Objects.equals(userModel.getPhoneNumbers(), "")) {
                textButton.setText("Press to enter phone number");
                textButton2.setText("Press to enter phone number");
                textButton3.setText("Press to enter phone number");
            } else {
                textButton.setText(userModel.getPhoneNumbers());
                textButton2.setText(userModel.getPhoneNumbers());
                textButton3.setText(userModel.getPhoneNumbers());
            }

        });
    }

    public void addPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPhoneNumber.this);
        builder.setTitle("Important Phone Numbers");
        builder.setMessage("Add new phone number");
        //builder.setMessage("Add name to phone number");
        builder.setCancelable(true);
        // Input field for user
        final EditText input = new EditText(this);
        builder.setView(input);
        // Done and cancel button
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = String.valueOf(input.getText());

                if (phoneNumber.isEmpty() || phoneNumber.length() >= 20) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Perform your desired operations with the userViewModel using the phoneNumber
                    userViewModel.userphoneNumbers(phoneNumber);
                    Toast.makeText(getApplicationContext(), "Phone number has been added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

*/