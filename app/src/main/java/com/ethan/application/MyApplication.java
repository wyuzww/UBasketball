package com.ethan.application;

import android.app.Application;

import com.mob.MobSDK;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initMobSDK();


    }

    private void initMobSDK() {
        MobSDK.init(this, "2711b513b7e98", "3f4956b452fd3d82671f707193bda8ef");
    }

}
