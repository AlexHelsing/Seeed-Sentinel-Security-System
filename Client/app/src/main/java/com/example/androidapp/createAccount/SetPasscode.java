package com.example.androidapp.createAccount;


import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.androidapp.KeypadUtils;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
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
                // get the mongo client, this will get moved somewhere else later
                MongoClient mongoClient = currentUser.getMongoClient("mongodb-atlas");
                MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

                // create a document to store our customData, just name and pattern for now. not sure how we are gonna deal with images yet. maybe just use urls?
                // cant use pictures from phone cus that requires you to store those images in a cloud storage somewhere :/
                //     Document doc = new Document("name", name)
                //          .append("pattern", pattern)
                //        .append("user-id", currentUser.getId());

                Document doc = new Document("user-id", currentUser.getId())
                        .append("name", name)
                        // hash the passcode

                        .append("passcode", TempPasscodeString)
                        .append("profilePic", profilePic)
                        .append("breakins", Arrays.asList());

                // insert the document
                mongoCollection.insertOne(doc).getAsync(result -> {
                    if (result.isSuccess()) {
                        Log.v("Data", "Successfully inserted a document with id: " + result.get().getInsertedId());
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("Data", "failed to insert document with: ", result.getError());
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