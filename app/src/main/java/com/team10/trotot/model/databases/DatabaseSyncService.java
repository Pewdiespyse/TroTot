package com.team10.trotot.model.databases;

/**
 * Created by Pewdiespyse on 21/11/2017.
 */

public class DatabaseSyncService {
    public static void syncLocalToFirebase() {
        CustomFirebaseDatabase customFirebaseDatabase = new CustomFirebaseDatabase();
        LocalDatabase localDatabase = new LocalDatabase();
        for (int i = 0; i < 5; i++) {
            customFirebaseDatabase.getUserModel().addAndUpdateUser(localDatabase.getLocalUserModer().getUser(i));
            customFirebaseDatabase.getMotelModel().addAndUpdateMotel(localDatabase.getLocalMotelModel().getMotel(i));
            customFirebaseDatabase.getNewsModel().addAndUpdateNews(localDatabase.getLocalNewsModel().getNews(i));
        }
    }
}
