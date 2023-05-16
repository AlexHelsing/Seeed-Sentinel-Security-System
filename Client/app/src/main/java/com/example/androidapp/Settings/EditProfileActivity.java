package com.example.androidapp.Settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.MQTT.BrokerConnection;
import com.example.androidapp.MyApp;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;

public class EditProfileActivity  extends AppCompatActivity {

    UserViewModel userViewModel;

    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofilelayout);

        dbHandler db = new dbHandler(getApplicationContext());


        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);
        MyApp myApp = (MyApp) getApplication();
        BrokerConnection brokerConnection = myApp.getBrokerConnection();


        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            finish();
        });

        EditText newName = findViewById(R.id.editTextName);
        Button saveButton = findViewById(R.id.buttonSave);
        EditText newPicture = findViewById(R.id.editpfp);
        Button savePicture = findViewById(R.id.pfpSave);

        saveButton.setOnClickListener(view -> {
            newName.getText().toString();

            if (newName.getText().toString().isEmpty()) {
                newName.setError("Please enter a name");
                newName.requestFocus();
            } else {
                userViewModel.editName(newName.getText().toString());
                brokerConnection.publishMqttMessage("/SeeedSentinel/GetUserProfile", newName.getText().toString(), "NameChange");
                finish();
            }
        });

        savePicture.setOnClickListener(view -> {
            newPicture.getText().toString();

            if (newPicture.getText().toString().isEmpty()) {
                newPicture.setError("Please enter a picture");
                newPicture.requestFocus();
            } else {
                userViewModel.editProfilePicture(newPicture.getText().toString());
                finish();
            }
        });

    }
}
