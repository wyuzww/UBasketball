package com.ethan.entity;

public class Video {
    private int video_id;
    private User user;
    private String video_title;
    private String video_pic;
    private String video_url;
    private String video_time;


    public Video() {
    }

    public Video(int video_id, User user, String video_title, String video_pic, String video_url, String video_time) {
        this.video_id = video_id;
        this.user = user;
        this.video_title = video_title;
        this.video_pic = video_pic;
        this.video_url = video_url;
        this.video_time = video_time;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_pic() {
        return video_pic;
    }

    public void setVideo_pic(String video_pic) {
        this.video_pic = video_pic;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }
}
