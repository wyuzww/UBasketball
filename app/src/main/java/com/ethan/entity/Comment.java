package com.ethan.entity;

import java.io.Serializable;

public class Comment implements Serializable {
    private int comment_id;
    private int mood_id;
    private User user;
    private String comment_text;
    private String comment_time;


    public Comment() {
    }

    public Comment(int comment_id, int mood_id, User user, String comment_text, String comment_time) {
        this.comment_id = comment_id;
        this.mood_id = mood_id;
        this.user = user;
        this.comment_text = comment_text;
        this.comment_time = comment_time;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
