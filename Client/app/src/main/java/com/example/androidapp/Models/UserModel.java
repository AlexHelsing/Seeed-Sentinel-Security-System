package com.example.androidapp.Models;

import org.bson.Document;

import java.util.Date;
import java.util.List;


public class UserModel {
    private String name;
    private String passcode;
    private String profileImg;

    private String wioLocation;

    private List<Document> breakins;
    private String phoneNumbers;

    public UserModel(String name, String passcode, String profileImg, List<Document> breakins, String wioLocation, String phoneNumbers) {
        this.name = name;
        this.passcode = passcode;
        this.profileImg = profileImg;
        this.breakins = breakins;
        this.wioLocation = wioLocation;
        this.phoneNumbers = phoneNumbers;
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

    //set passcode
    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public List<Document> getBreakins() {
        return breakins;
    }

    // add breakin
    public void addBreakin(Document breakin) {
        this.breakins.add(breakin);
    }


    // set breakins
    public void setBreakins(List<Document> breakins) {
        this.breakins = breakins;
    }

    public String getProfileImg() {
        return profileImg;
    }

    // set profile image
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getWioLocation() {
        return wioLocation;
    }

    public void setWioLocation(String wioLocation) {
        this.wioLocation = wioLocation;
    }
    public void setPhoneNumbers(String phoneNumbers){this.phoneNumbers = phoneNumbers;}
    public String getPhoneNumbers(){return phoneNumbers;}

}
