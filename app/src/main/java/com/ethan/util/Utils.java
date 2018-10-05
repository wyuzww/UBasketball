package com.ethan.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.entity.User;
import com.ethan.util.network.HttpClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private SharedPreferences user_info_preferences;
    private SharedPreferences.Editor editor;


    public void saveUser(Context context, User user) {
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

    public boolean isSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    public File createImageFile(Context context, File mfile, String type, String user_number) {
        if (isSdcard()) {
            File file = context.getExternalFilesDir(type);
            mfile = new File(file.getPath(), user_number + ".jpg");
            try {
                if (mfile.exists()) {
                    mfile.delete();
                    //getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DATA + "=?",new String[]{mImageFile.getPath()});

                }
                mfile.createNewFile();
                return mfile;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                return null;
            }

        } else {
            Toast.makeText(context, "找不到SD卡，容易造成数据丢失", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void SaveImage(Context context, File temp_file, String type, String user_number) {
//        Toast.makeText(this,"save1",Toast.LENGTH_SHORT).show();
        File userImage = null;
        userImage = createImageFile(context, userImage, type, user_number);
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        byte[] bytes = new byte[1024];
        int lenth = 0;
        try {
            if (temp_file != null && userImage != null) {
                if (temp_file.isFile() || userImage.isFile()) {
                    inputStream = new FileInputStream(temp_file);
                    outputStream = new FileOutputStream(userImage);
                    while ((lenth = inputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, lenth);
                    }
                    temp_file.delete();
                }
            } else {
                Toast.makeText(context, "没找到SD卡，无法保存图片", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void findUserImage(final String user_image, String user_number, final Activity activity, final ImageView view) {
        final File file = new File(activity.getExternalFilesDir("userImage"), user_number + ".jpg");
        final String ImagePath = new HttpClient().getURL() + user_image;
        //user_image_IV.setImageBitmap();//头像
        if (file.exists()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(activity.getBaseContext()).invalidate(file);
                    Picasso.with(activity).load(file)
                            .into(view);
                }
            });

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {//http://192.168.43.196/UBasketball/     "http://wyuzww.nat123.net/UBasketball/userImage/13612250853.jpg"
                    Picasso.with(activity).invalidate(ImagePath);
                    Picasso.with(activity).load(ImagePath)
                            .error(R.mipmap.ic_logo)
                            .placeholder(R.mipmap.ic_logo)
                            .into(view);
                }
            });
        }
    }


}
