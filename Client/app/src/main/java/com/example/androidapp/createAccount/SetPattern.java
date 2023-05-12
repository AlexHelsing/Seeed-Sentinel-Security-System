package com.example.androidapp.createAccount;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class SetPattern extends AppCompatActivity {

    dbHandler db;
    App app;
    User currentUser;

    Bundle extra;

    String name;
    String profilePic;
    Button submitButton;

    PatternLockView mPatternLockView;


    private PatternLockViewListener mPatternLockViewListener2 = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
            // if pattern is greater than 3, make it green ie its long enough for our purposes IMO :=)
            if (progressPattern.size() >= 3) {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
            } else {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
            }
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));

        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_setpattern);


        // get the name from the previous activity
        extra = getIntent().getExtras();
        name = extra.getString("name");
        profilePic = extra.getString("profilePic");


        db = new dbHandler(getApplicationContext());
        app = db.getApp();
        currentUser = app.currentUser();

        submitButton = findViewById(R.id.submitButton);

        mPatternLockView = findViewById(R.id.pattern_lock_view1);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener2);


        submitButton.setOnClickListener(view -> {
            String pattern = PatternLockUtils.patternToString(mPatternLockView, mPatternLockView.getPattern());
            Log.v("pattern", pattern);

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
                      .append("passcode", pattern)
                       .append("pattern", pattern)
                      .append("profilePic", profilePic)
                        // create empty array of objects for the friends list
                        .append("breakins", Arrays.asList())
                    ;



            // insert the document
            mongoCollection.insertOne(doc).getAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("Data", "Successfully inserted a document with id: " + result.get().getInsertedId());
                    Intent intent = new Intent(SetPattern.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("Data", "failed to insert document with: ", result.getError());
                }
            });
        });
    }
}