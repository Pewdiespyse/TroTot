package com.team10.trotot.model.databases;

import com.team10.trotot.model.databases.firebase_models.MotelModel;
import com.team10.trotot.model.databases.firebase_models.NewsModel;
import com.team10.trotot.model.databases.firebase_models.UserModel;

/**
 * Created by Pewdiespyse on 13/11/2017.
 */

public class CustomFirebaseDatabase {
    UserModel userModel;
    MotelModel motelModel;
    //RatingModel ratingModel;
    //CommentModel commentModel;
    NewsModel newsModel;

    public CustomFirebaseDatabase() {
        userModel = new UserModel();
        motelModel = new MotelModel();
        newsModel = new NewsModel();
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public MotelModel getMotelModel() {
        return motelModel;
    }

    public NewsModel getNewsModel() {
        return newsModel;
    }
}
