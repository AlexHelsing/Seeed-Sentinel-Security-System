package com.example.androidapp;

public class UserModel {
    private String name;
    private String passcode;
    private String profileImg;

    public UserModel(String name, String passcode, String profileImg) {
        this.name = name;
        this.passcode = passcode;
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasscode() {
        return passcode;
    }


    public String getProfileImg() {
        return profileImg;
    }
}
