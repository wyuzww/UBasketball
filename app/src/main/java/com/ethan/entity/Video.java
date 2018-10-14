package com.ethan.entity;

public class Video {
    private String video_title;
    private String video_pic;
    private String video_url;

    public Video() {
    }

    public Video(String video_title, String video_pic, String video_url) {
        this.video_title = video_title;
        this.video_pic = video_pic;
        this.video_url = video_url;
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
}
