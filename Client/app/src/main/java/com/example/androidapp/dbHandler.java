package com.example.androidapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.androidapp.Models.UserModel;
import com.example.androidapp.ViewModels.UpdateUserDataCallback;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


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

        if (app.currentUser() == null) {
            CurrentUser = null;
        }
        CurrentUser = app.currentUser();

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
                String passcode = customData.getString("passcode");
                String profilePic = customData.getString("profilePic");
                List<Document> breakins = customData.getList("breakins", Document.class );

                UserModel userModel = new UserModel(name, passcode, profilePic, breakins);

                return userModel;


            }

        }

        return null;
    }

    // CRUD operations for user data

    // update username
    public void updateUsername(String newname, UpdateUserDataCallback callback) {
        User user = app.currentUser();
        if (user != null) {
            // not sure if this line even does anything but i'll leave it here
            user.getCustomData().put("name", newname);

            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

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

    // update passcode
    public void updatePasscode(String newpasscode, UpdateUserDataCallback callback) {
        User user = app.currentUser();
        if (user != null) {
            user.getCustomData().put("passcode", newpasscode);
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

            mongoCollection.updateOne(new Document("user-id", user.getId()), new Document("$set", new Document("passcode", newpasscode))).getAsync(
                    result -> {
                        if (result.isSuccess()) {
                            System.out.println("successfully updated passcode");
                            callback.onSuccess();
                        } else {
                            System.out.println("failed to update passcode");
                            callback.onError();
                        }
                    }
            );
        }

    }

    // add breakin to db
    public void createBreakInAlert(String location, Date date, UpdateUserDataCallback callback) {
        User user = app.currentUser();
        if (user != null) {
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

            mongoCollection.updateOne(new Document("user-id", user.getId()), new Document("$push", new Document("breakins", new Document("location", location).append("date", date)))).getAsync(
                    result -> {
                        if (result.isSuccess()) {
                            System.out.println("successfully added breakin");
                            callback.onSuccess();
                        } else {
                            System.out.println("failed to add breakin");
                            callback.onError();
                        }
                    }
            );


        }
    }

    // update profile picture
    public void updateProfilePicture(String profilePicture, UpdateUserDataCallback updateUserDataCallback) {
        User user = app.currentUser();
        if (user != null) {
            user.getCustomData().put("profilePic", profilePicture);
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");


            mongoCollection.updateOne(new Document("user-id", user.getId()), new Document("$set", new Document("profilePic", profilePicture))).getAsync(
                    result -> {
                        if (result.isSuccess()) {
                            System.out.println("successfully updated profile picture");
                            updateUserDataCallback.onSuccess();
                        } else {
                            System.out.println("failed to update profile picture");
                            updateUserDataCallback.onError();
                        }
                    }
            );
        }
    }


    //Stati function to set users custom data at the last page of onboarding process.
    public static void setCustomData(User user, String name, String passcode, String profilePic, UpdateUserDataCallback callback) {

        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

        Document doc = new Document("user-id", user.getId())
                .append("name", name)
                .append("passcode", passcode)
                .append("profilePic", profilePic)
                .append("breakins", Arrays.asList());

        // insert the document
        mongoCollection.insertOne(doc).getAsync(result -> {
            if (result.isSuccess()) {
                Log.v("Data", "Successfully inserted a document with id: " + result.get().getInsertedId());
                callback.onSuccess();
            } else {
                Log.e("Data", "failed to insert document with: ", result.getError());
                callback.onError();
            }
        });
    }
}










