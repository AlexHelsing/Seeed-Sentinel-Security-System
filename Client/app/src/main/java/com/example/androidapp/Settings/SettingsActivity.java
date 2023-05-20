package com.example.androidapp.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import java.util.Objects;

import io.realm.mongodb.App;

public class SettingsActivity extends AppCompatActivity {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout addPhonenumberBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    LinearLayout navigateToSetNotifications;
    AppCompatButton editProfileBtn;
    UserViewModel userViewModel;
    TextView editPhoneNrText;

    @RequiresApi(api = Build.VERSION_CODES.O)
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


        //navigate to notification channels
        navigateToSetNotifications = findViewById(R.id.navigateToSetNotifications);
        navigateToSetNotifications.setOnClickListener(view ->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);

            }
        });
        editPhoneNrText = findViewById(R.id.editPhoneNrText);
        userViewModel.getUser().observe(this, userModel -> {
            if(userModel.getPhoneNumbers() == null || Objects.equals(userModel.getPhoneNumbers(), "")){
                editPhoneNrText.setText("Enter emergency contact phone number");
            }
            else{
                editPhoneNrText.setText(userModel.getPhoneNumbers());
            }}

        );

    }

    public void addPhoneNumber() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Important Phone Numbers");
        builder.setMessage("Add new phone number");
        //builder.setMessage("Add name to phone number");
        builder.setCancelable(true);
        // Input field for user
        final EditText input = new EditText(this);
        builder.setView(input);
        // Done and cancel button
        builder.setPositiveButton("Done", (dialog, which) -> {
            String phoneNumber = String.valueOf(input.getText());

            if (phoneNumber.length() == 0 || phoneNumber.length() >= 20) {
                Toast.makeText(getApplicationContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            }
            else {
                userViewModel.userphoneNumbers(phoneNumber);
                editPhoneNrText.setText(phoneNumber);
                Toast.makeText(getApplicationContext(), "Phone number " + phoneNumber + " has been added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
