package com.team10.trotot.model.databases;

import com.team10.trotot.model.databases.local_models.LocalMotelModel;
import com.team10.trotot.model.databases.local_models.LocalNewsModel;
import com.team10.trotot.model.databases.local_models.LocalUserModel;

/**
 * Created by Pewdiespyse on 13/11/2017.
 */

public class LocalDatabase {

    LocalUserModel localUserModel;
    LocalMotelModel localMotelModel;
    //LocalRatingModel localRatingModel;
    //LocalCommentModel localCommentModel;
    LocalNewsModel localNewsModel;

    public LocalDatabase() {
        localUserModel = new LocalUserModel();
        localMotelModel = new LocalMotelModel();
        localNewsModel = new LocalNewsModel();
    }

    public LocalUserModel getLocalUserModer() {
        return localUserModel;
    }

    public LocalMotelModel getLocalMotelModel() {
        return localMotelModel;
    }

    public LocalNewsModel getLocalNewsModel() {
        return localNewsModel;
    }
}
