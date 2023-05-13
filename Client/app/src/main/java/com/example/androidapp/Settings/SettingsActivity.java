package com.example.androidapp.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.StarterPage;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;
import com.squareup.picasso.Picasso;

import io.realm.mongodb.App;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    dbHandler db;
    App app;
    ImageView backArrow;
    LinearLayout navigateToPatternBtn;
    LinearLayout navigateToPasscodeBtn;
    LinearLayout LogOutButton;
    AppCompatButton editProfileBtn;
    private static final String CHANNEL_ID = "AlarmStatus";
    public static final String CHANNEL_ID2 = "AlarmStatusSilent";
    private Context context;



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
                    Picasso.get().load(userModel.getProfileImg()).into(profilePic);
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


        // navigate to set pattern activity
        navigateToPatternBtn = findViewById(R.id.NavigateToSetPattern);
        navigateToPatternBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePattern.class);
            startActivity(intent);
        });

        Spinner spinnerNotifications=findViewById(R.id.spinner_notifications);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.notifications, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotifications.setAdapter(adapter);
        spinnerNotifications.setOnItemSelectedListener(this);

        context = getApplicationContext();

    }
    String channelId = MainActivity.getChannelId();
    String channelId2 = MainActivity.getChannelId2();
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("Spinner", "Item selected: " + adapterView.getItemAtPosition(position).toString());
        String selectedItem = adapterView.getItemAtPosition(position).toString();

        switch (position) {
            case 0:
                //BrokerConnection brokerConnection = new BrokerConnection(context);
                //brokerConnection.sendIntruderNotification(selectedItem);
                break;
            case 1:
                // User selected "Send notification"
                BrokerConnection brokerConnection = new BrokerConnection(context);
                brokerConnection.sendIntruderNotification(selectedItem);
                break;
            default:
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}
