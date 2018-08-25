package com.ethan.entity;

public class User {
    private int user_id;
    private String user_number;
    private String user_name;
    private String user_password;
    private String user_sex;
    private String user_birth;
    private String user_signature;
    private String user_token;


    public void setUser(User user) {
        this.user_id = user.user_id;
        this.user_number = user.user_number;
        this.user_name = user.user_name;
        this.user_password = user.user_password;
        this.user_sex = user.user_sex;
        this.user_birth = user.user_birth;
        this.user_token = user.user_token;
        this.user_signature = user.user_signature;

    }

    public int getUser_id() {
        return user_id;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public String getUser_number() {
        return user_number;
    }


    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }


    public String getUser_password() {
        return user_password;
    }


    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }


    public String getUser_token() {
        return user_token;
    }


    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }


    public String getUser_name() {
        return user_name;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_signature() {
        return user_signature;
    }

    public void setUser_signature(String user_signature) {
        this.user_signature = user_signature;
    }
}
