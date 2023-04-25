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

public class CustomizeProfile extends AppCompatActivity {

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
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
            Document updateDocument = new Document().append("name", nameText);
            mongoCollection.insertOne(
                    new Document("user-id", user.getId()).append("name", nameText) // i guess we just replace the existing one for now :/
            ).getAsync(result -> {
                if (result.isSuccess()) {
                    Toast.makeText(getApplicationContext(), "Successfully updated profile.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
