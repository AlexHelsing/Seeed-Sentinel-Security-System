package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.mongodb.App;
import android.widget.Button;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

public class setNamePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_profile);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();
        User user = app.currentUser();

        EditText name = findViewById(R.id.name_input);
        Button saveButton = findViewById(R.id.save_profile_changes);

        saveButton.setOnClickListener(v -> {
            String nameText = name.getText().toString();
            // put nametext as extra in intent
            // send intent to dashboard
            Intent intent = new Intent(getApplicationContext(), setPatternPage.class);
            intent.putExtra("name", nameText);
            startActivity(intent);
        });



    }
}
