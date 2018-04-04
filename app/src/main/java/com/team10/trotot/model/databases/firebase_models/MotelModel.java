package com.team10.trotot.model.databases.firebase_models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.model.basic_classes.Motel;

/**
 * Created by Pewdiespyse on 07/11/2017.
 */

public class MotelModel {
    private DatabaseReference dataNodeMotel;

    public MotelModel() {
        dataNodeMotel = FirebaseDatabase.getInstance().getReference().child("motels");
    }

    //Add a motel to firebase
    public void addAndUpdateMotel(Motel motel) {
        dataNodeMotel.child(motel.getMotelId()).setValue(motel);
    }

    //Delete an specific motel on firebase
    public void deleteMotel(String Id) {
        dataNodeMotel.child(Id).setValue(null);
    }
}
