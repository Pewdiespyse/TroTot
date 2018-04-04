package com.team10.trotot.model.basic_classes;

/**
 * Created by vinhkhang on 20/11/2017.
 */

public class Reply {

    private long time;
    private String content;
    private String userId;

    public Reply() {
    }

    public Reply(long time, String content, String userId) {
        this.time = time;
        this.content = content;
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "time=" + time +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
