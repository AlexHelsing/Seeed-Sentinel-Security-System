package com.example.androidapp;

import android.content.Context;
import android.util.Log;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.atomic.AtomicBoolean;


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
            // refresh first
            user.refreshCustomData(result -> {
                if (result.isSuccess()) {
                    Log.v("AUTH", "Successfully refreshed custom data.");
                } else {
                    Log.e("AUTH", "Failed to refresh custom data.");
                }
            });
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


    // edit users name
    public void updateUsername(String newname, UpdateUserNameCallback callback) {
        User user = app.currentUser();
        if (user != null) {
            user.getCustomData().put("name", newname);

            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

            // update the document with the new name return the success status using callback so i can update ui
            mongoCollection.updateOne(new Document("user-id", user.getId()), new Document("$set", new Document("name", newname))).getAsync(
                    result -> {
                        if (result.isSuccess()) {
                            System.out.println("successfully updated name");
                            callback.onSuccess();
                        } else {
                            System.out.println("failed to update name");
                            callback.onError();
                        }
                    }
            );
        }

    }
}










