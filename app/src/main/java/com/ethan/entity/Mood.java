package com.ethan.entity;

import java.util.ArrayList;

public class Mood {
    private int mood_id;
    private User user;
    private String mood_text;
    private String mood_time;
    private int mood_images_amount;
    private int mood_clocks_amount;
    private int mood_comments_amount;
    private int mood_loves_amount;
    private ArrayList<String> mood_images_url;

    public Mood(int mood_id, User user, String mood_text, String mood_time, int mood_images_amount, int mood_clocks_amount, int mood_comments_amount, int mood_loves_amount, ArrayList<String> mood_images_url) {
        this.mood_id = mood_id;
        this.user = user;
        this.mood_text = mood_text;
        this.mood_time = mood_time;
        this.mood_images_amount = mood_images_amount;
        this.mood_clocks_amount = mood_clocks_amount;
        this.mood_comments_amount = mood_comments_amount;
        this.mood_loves_amount = mood_loves_amount;
        this.mood_images_url = mood_images_url;
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

    public String getMood_text() {
        return mood_text;
    }

    public void setMood_text(String mood_text) {
        this.mood_text = mood_text;
    }

    public String getMood_time() {
        return mood_time;
    }

    public void setMood_time(String mood_time) {
        this.mood_time = mood_time;
    }

    public int getMood_images_amount() {
        return mood_images_amount;
    }

    public void setMood_images_amount(int mood_images_amount) {
        this.mood_images_amount = mood_images_amount;
    }

    public int getMood_clocks_amount() {
        return mood_clocks_amount;
    }

    public void setMood_clocks_amount(int mood_clocks_amount) {
        this.mood_clocks_amount = mood_clocks_amount;
    }

    public int getMood_comments_amount() {
        return mood_comments_amount;
    }

    public void setMood_comments_amount(int mood_comments_amount) {
        this.mood_comments_amount = mood_comments_amount;
    }

    public int getMood_loves_amount() {
        return mood_loves_amount;
    }

    public void setMood_loves_amount(int mood_loves_amount) {
        this.mood_loves_amount = mood_loves_amount;
    }

    public ArrayList<String> getMood_images_url() {
        return mood_images_url;
    }

    public void setMood_images_url(ArrayList<String> mood_images_url) {
        this.mood_images_url = mood_images_url;
    }
}
