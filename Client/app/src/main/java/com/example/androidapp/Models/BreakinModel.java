package com.example.androidapp.Models;

import java.util.Date;

// dont know if we need this class. might be able to just use a document
public class BreakinModel {
    private String location;
    private Date date;

    public BreakinModel(String location, Date date) {
        this.location = location;
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

