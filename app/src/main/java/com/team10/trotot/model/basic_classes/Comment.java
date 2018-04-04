package com.team10.trotot.model.basic_classes;

import java.util.List;

/**
 * Created by vinhkhang on 20/11/2017.
 */

public class Comment {
    private long time;
    private String userId;
    private String content;
    private List<String> likes;
    private List<Reply> replies;

    public Comment() {
    }

    public Comment(long time, String userId, String content, List<String> likes, List<Reply> replies) {
        this.time = time;
        this.userId = userId;
        this.content = content;
        this.likes = likes;
        this.replies = replies;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "time=" + time +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", replies=" + replies +
                '}';
    }
}
