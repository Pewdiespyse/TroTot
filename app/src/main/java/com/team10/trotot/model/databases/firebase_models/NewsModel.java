package com.team10.trotot.model.databases.firebase_models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.model.basic_classes.News;

/**
 * Created by Pewdiespyse on 07/11/2017.
 */

public class NewsModel {
    private DatabaseReference dataNodeNews;

    public NewsModel() {
        dataNodeNews = FirebaseDatabase.getInstance().getReference().child("news");
    }

    //Add a news to firebase
    public void addAndUpdateNews(News news) {
        dataNodeNews.child(news.getNewsId()).setValue(news);
    }

    //Delete an specific motel on firebase
    public void deleteMotel(String Id) {
        dataNodeNews.child(Id).setValue(null);
    }

}