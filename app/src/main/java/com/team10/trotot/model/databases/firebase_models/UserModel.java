package com.team10.trotot.model.databases.firebase_models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.model.basic_classes.User;

/**
 * Created by Pewdiespyse on 07/11/2017.
 */

public class UserModel {
    private DatabaseReference dataNodeUser;

    //Auto get data from firebase
    public UserModel() {
        dataNodeUser = FirebaseDatabase.getInstance().getReference().child("users");
    }

    //Add a user to firebase
    public void addAndUpdateUser(User user) {
        dataNodeUser.child(user.getUserId()).setValue(user);
    }

    //Delete an specific user on firebase
    public void deleteUser(String Id) {
        dataNodeUser.child(Id).setValue(null);
    }
}