package com.ethan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ethan.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {


    public void saveUser(Context context, User user) {
        SharedPreferences user_info_preferences;
        SharedPreferences.Editor editor;
        user_info_preferences = context.getSharedPreferences("UserInfo", 0);
        editor = user_info_preferences.edit();
        editor.putBoolean("isLogined", true);
        editor.putInt("user_id", user.getUser_id());
        editor.putString("user_name", user.getUser_name());
        editor.putString("user_number", user.getUser_number());
        editor.putString("user_password", user.getUser_password());
        editor.putString("user_sex", user.getUser_sex());
        editor.putString("user_birth", user.getUser_birth());
        editor.putString("user_signature", user.getUser_signature());
        editor.putString("user_token", user.getUser_token());
        editor.apply();
    }


    public String md5Password(String password) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            for (byte b : result) {
                //与运算
                int number = b & 0xff;
                String string = Integer.toHexString(number);
                if (string.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(string);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
