package com.example.androidapp;

import android.content.Context;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;



public class dbHandler {

    // should probably hide this later :)
    private final String APP_ID = "seeedsentinelrealm-nfhhn";
    private App app;
    private static User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private CurrentUserData currentUserData;


    public dbHandler(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());
    }

    public App getApp() {
        return app;
    }


    // maybe this is a bad idea since we can just use currentUser().getCustomData()
    public CurrentUserData getCurrentUserData() {
        return currentUserData;
    }


}










