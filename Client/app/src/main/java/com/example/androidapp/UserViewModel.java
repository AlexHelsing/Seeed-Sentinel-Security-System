package com.example.androidapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.mongodb.User;

public class UserViewModel extends ViewModel{

    private MutableLiveData<UserModel> _user = new MutableLiveData<>();
    private dbHandler db;


    public UserViewModel(dbHandler db) {
        this.db = db;
        User currentUser = db.getCurrentUser();
        if (currentUser.isLoggedIn()) {
            _user.setValue(db.getUserData());
        }
    }

    public LiveData<UserModel> getUser() {
        return _user;
    }
}
