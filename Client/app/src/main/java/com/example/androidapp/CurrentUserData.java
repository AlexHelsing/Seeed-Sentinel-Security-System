package com.example.androidapp;


// might change into realm object later, idk fully how that works yet :/
public class CurrentUserData {
    String name;
    String pattern;


    public CurrentUserData(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "CurrentUserData{" +
                "name='" + name + '\'' +
                ", pattern='" + pattern + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
