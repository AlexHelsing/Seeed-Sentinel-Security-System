package com.example.androidapp;

import android.content.Context;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class dbHandler {

    // should probably hide this later :)
    private static final String APP_ID = "seeedsentinelrealm-nfhhn";
    private static dbHandler instance;
    private static App app;
    private static User user;


    public dbHandler(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());
    }

    public App getApp() {
        return app;
    }

    public User getUser() {
        return user;
    }
}
