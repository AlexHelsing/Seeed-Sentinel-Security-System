package com.example.androidapp.ViewModels;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.androidapp.Models.UserModel;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.User;
import org.bson.Document;

import java.util.Date;


public class UserViewModel extends ViewModel{

    private static MutableLiveData<UserModel> _user = new MutableLiveData<>();
    private static dbHandler db;


    public UserViewModel(dbHandler db) {
        UserViewModel.db = db;
        User currentUser = db.getCurrentUser();
        if (currentUser != null) {
            _user.setValue(db.getUserData());
        }
    }


    public LiveData<UserModel> getUser() {
        // if null, throw error
        if (_user.getValue() == null) {
            Log.e("AUTH", "User is null.");
        }

        return _user;
    }

    // static method to get _user.passcode
    public static String getEmergencyContactNumber() {
        return _user.getValue().getPhoneNumbers();
    }


    //edit name of user
    public void editName(String name) {
        db.updateUsername(name, new UpdateUserDataCallback() {
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

    public void editWioLocation(String wioLocation) {
        db.updateWioLocation(wioLocation, new UpdateUserDataCallback() {
            @Override
            public void onSuccess() {
                Log.v("AUTH", "Successfully updated wio location.");
                UserModel userModel = db.getUserData();
                userModel.setWioLocation(wioLocation);
                _user.setValue(userModel);

            }
            @Override
            public void onError() {
                Log.e("AUTH", "Failed to update wio location.");
            }
        });

    }

    public void editPhoneNumber(String phoneNumbers){
        db.updatePhoneNumbers(phoneNumbers, new UpdateUserDataCallback() {
            @Override
            public void onSuccess() {
                Log.v("AUTH", "Successfully updated saved phonenumbers.");
                UserModel userModel = db.getUserData();
                userModel.setPhoneNumbers(phoneNumbers);
                _user.setValue(userModel);
            }

            @Override
            public void onError() {
                Log.e("AUTH", "Failed to update users saved phone number.");
            }
        });
    }





    // edit passcode of user (hash this later if we have time
    public void editPasscode (String passcode) {
        db.updatePasscode(passcode, new UpdateUserDataCallback() {
            @Override
            public void onSuccess() {

                Log.v("AUTH", "Successfully updated passcode.");
                UserModel userModel = db.getUserData();
                userModel.setPasscode(passcode);
                _user.setValue(userModel);
            }

            @Override
            public void onError() {
                Log.e("AUTH", "Failed to update passcode.");
            }
        });
    }

    //add breakin to the array of breakins
    public static void createBreakin(Date date) {
        db.createBreakInAlert(_user.getValue().getWioLocation(), date, new UpdateUserDataCallback() {
            @Override
            public void onSuccess() {
                Log.v("AUTH", "Successfully created breakin.");
                UserModel userModel = db.getUserData();
                userModel.addBreakin(new Document("location", _user.getValue().getWioLocation()).append("date", date));
                _user.setValue(userModel);
            }

            @Override
            public void onError() {
                Log.e("AUTH", "Failed to create breakin.");
            }
        });
    }



    // modify the profile picture of the user
    public void editProfilePicture(String profilePicture) {
        db.updateProfilePicture(profilePicture, new UpdateUserDataCallback() {
            @Override
            public void onSuccess() {
                Log.v("AUTH", "Successfully updated profile picture.");
                UserModel userModel = db.getUserData();
                userModel.setProfileImg(profilePicture);
                _user.setValue(userModel);
            }

            @Override
            public void onError() {
                Log.e("AUTH", "Failed to update profile picture.");
            }
        });
    }

    // clear user data from viewmodel, might not need this
    public void clear() {
        _user.setValue(null);
    }
}
