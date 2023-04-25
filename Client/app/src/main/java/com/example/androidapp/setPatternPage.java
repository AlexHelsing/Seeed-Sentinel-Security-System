package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.List;


public class setPatternPage extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private String name;

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
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.set_pattern_page);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String nameExtra = extra.getString("name");
            name = nameExtra;
        }


        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view_onboarding2);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener2);

        ImageView goBack = findViewById(R.id.back_button_onboarding2);
        goBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), setNamePage.class);
            startActivity(intent);
        });


        Button continueButton = findViewById(R.id.continue_button_onboarding2);
        continueButton.setOnClickListener(view -> {
            String pattern = PatternLockUtils.patternToString(mPatternLockView, mPatternLockView.getPattern());
            Log.v("setPatternPage", "pattern: " + pattern);

            User user = app.currentUser();
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

            Document doc = new Document("name", name)
                    .append("pattern", pattern)
                    .append("user-id", user.getId());

            mongoCollection.insertOne(doc).getAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("setPatternPage", "successfully inserted document");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.v("setPatternPage", "failed to insert document with: " + result.getError().toString());
                }
            });
        });




    }
}


