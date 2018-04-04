package com.team10.trotot.model.basic_classes;

/**
 * Created by vinhkhang on 20/11/2017.
 */

public class Rating {

    private int score;
    private String userId;

    public Rating() {
    }

    public Rating(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
