package com.team10.trotot.model.basic_classes;

/**
 * Created by Pewdiespyse on 21/11/2017.
 */

public class News {
    String newsId;
    String title;
    String content;
    long timePost;

    public News() {
    }

    public News(String newsId, String title, String content, long timePost) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.timePost = timePost;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimePost() {
        return timePost;
    }

    public void setTimePost(long timePost) {
        this.timePost = timePost;
    }
}
