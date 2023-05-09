package com.example.androidapp.Settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidapp.R;
import com.example.androidapp.UserViewModel;
import com.example.androidapp.UserViewModelFactory;
import com.example.androidapp.dbHandler;

public class EditProfileActivity  extends AppCompatActivity {

    UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofilelayout);

        dbHandler db = new dbHandler(getApplicationContext());


        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(db)).get(UserViewModel.class);




        EditText newName = findViewById(R.id.editTextName);

        Button saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(view -> {
            newName.getText().toString();

            if (newName.getText().toString().isEmpty()) {
                newName.setError("Please enter a name");
                newName.requestFocus();
            } else {
                userViewModel.editName(newName.getText().toString());
                finish();
            }
        });

    }
}
