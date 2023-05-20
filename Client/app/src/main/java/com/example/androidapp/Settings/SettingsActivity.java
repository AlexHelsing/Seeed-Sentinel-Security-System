package com.example.androidapp.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;


import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;
import com.squareup.picasso.Picasso;

import io.realm.mongodb.App;

public class SettingsActivity extends AppCompatActivity {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout addPhonenumberBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    UserViewModel userViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);


        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);

        TextView username = findViewById(R.id.user_name);
        ImageView profilePic = findViewById(R.id.profilePicture);


        userViewModel.getUser().observe(this, userModel -> {
                    username.setText(userModel.getName());
                    Picasso.get().load(userModel.getProfileImg()).resize(500,500).into(profilePic);
                }

        );



        LogOutButton = findViewById(R.id.LogOutButton);
        LogOutButton.setOnClickListener(view -> {
            app.currentUser().logOutAsync(result -> {

                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully logged out.");
                    // clear the viewmodel data
                    Intent intent = new Intent(getApplicationContext(), StarterPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("AUTH", "Failed to log out, error: " + result.getError().getErrorMessage());
                }
            });
        });

        addPhonenumberBtn = findViewById(R.id.navigateToPhoneNumbers);
        addPhonenumberBtn.setOnClickListener(view -> {
            addPhoneNumber();

        });


        // edit profile button
        editProfileBtn = findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // open a dialog when user clicks on edit profile button

        // navigate to change passcode activity
        navigateToPasscodeBtn = findViewById(R.id.navigateToSetKeyword);
        navigateToPasscodeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasscode.class);
            startActivity(intent);
        });


        // return to dashboard
        backArrow = findViewById(R.id.back_button);
        backArrow.setOnClickListener(view -> {
            // start alarm status activity
            finish();
        });


    }

    public void addPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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
                    Toast.makeText(getApplicationContext(), "Phone number " + phoneNumber + " has been added", Toast.LENGTH_SHORT).show();
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
