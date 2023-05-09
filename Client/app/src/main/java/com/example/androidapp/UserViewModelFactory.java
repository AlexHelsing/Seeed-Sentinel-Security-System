package com.example.androidapp;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModelFactory  implements ViewModelProvider.Factory{

    private final dbHandler db;

    public UserViewModelFactory(dbHandler db) {
        this.db = db;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(db);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
