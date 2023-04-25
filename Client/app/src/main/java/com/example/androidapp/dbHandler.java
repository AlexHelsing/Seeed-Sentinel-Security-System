package com.example.androidapp;

import android.content.Context;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Hashtable;


public class dbHandler {

    // should probably hide this later :)
    private static final String APP_ID = "seeedsentinelrealm-nfhhn";
    private static dbHandler instance;
    private static App app;
    private static User user;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private CurrentUserData currentUserData;


    public dbHandler(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());
    }

    public App getApp() {
        return app;
    }


    // maybe this is a bad idea since we can just use currentUser().getCustomData() but idk
    public CurrentUserData getCurrentUserData() {
        return currentUserData;
    }


}










