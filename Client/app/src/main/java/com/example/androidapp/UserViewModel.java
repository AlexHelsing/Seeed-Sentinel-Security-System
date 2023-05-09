package com.example.androidapp;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.mongodb.User;


interface UpdateUserNameCallback {
    void onSuccess();
    void onError();
}

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

    //set name
    public void editName(String name) {
        db.updateUsername(name, new UpdateUserNameCallback() {
            @Override
            public void onSuccess() {
                Log.v("AUTH", "Successfully updated name.");
                UserModel userModel = db.getUserData();
                userModel.setName(name);
                _user.setValue(userModel);
            }

            @Override
            public void onError() {
                Log.e("AUTH", "Failed to update name.");
            }
        });

    }
}
