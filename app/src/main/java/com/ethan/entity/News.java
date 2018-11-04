package com.ethan.entity;

public class News {
    private int news_id;
    private String news_title;
    private String news_date;
    private String news_author;
    private String news_url;
    private String news_pic_url1;
    private String news_pic_url2;
    private String news_pic_url3;

    public News() {
    }

    public News(int news_id, String news_title, String news_date, String news_author, String news_url, String news_pic_url1, String news_pic_url2, String news_pic_url3) {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_date = news_date;
        this.news_author = news_author;
        this.news_url = news_url;
        this.news_pic_url1 = news_pic_url1;
        this.news_pic_url2 = news_pic_url2;
        this.news_pic_url3 = news_pic_url3;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_author() {
        return news_author;
    }

    public void setNews_author(String news_author) {
        this.news_author = news_author;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getNews_pic_url1() {
        return news_pic_url1;
    }

    public void setNews_pic_url1(String news_pic_url1) {
        this.news_pic_url1 = news_pic_url1;
    }

    public String getNews_pic_url2() {
        return news_pic_url2;
    }

    public void setNews_pic_url2(String news_pic_url2) {
        this.news_pic_url2 = news_pic_url2;
    }

    public String getNews_pic_url3() {
        return news_pic_url3;
    }

    public void setNews_pic_url3(String news_pic_url3) {
        this.news_pic_url3 = news_pic_url3;
    }
}
