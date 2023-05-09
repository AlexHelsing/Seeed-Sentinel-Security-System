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




public class dbHandler {

    // should probably hide this later :)
    private final String APP_ID = "seeedsentinelrealm-nfhhn";
    private App app;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private User CurrentUser;


    public dbHandler(Context context) {
        // use singleton pattern to get instance of db : TODO :

        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());

        if (app.currentUser() != null) {
            CurrentUser = app.currentUser();
        }




    }

    public App getApp() {
        return app;
    }

    public User getCurrentUser() {
        return CurrentUser;
    }


    public UserModel getUserData() {
        User user = app.currentUser();
        if (user != null) {
            Document customData =  user.getCustomData();
            if (customData != null) {
                String name = customData.getString("name");
                String pattern = customData.getString("pattern");

                UserModel userModel = new UserModel(name, pattern, null);

                return userModel;


            }

        }

        return null;
    }



}










