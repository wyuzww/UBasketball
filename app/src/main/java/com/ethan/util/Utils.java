package com.ethan.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.ethan.R;
import com.ethan.entity.User;
import com.ethan.util.network.HttpClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
//                Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                return null;
            }

        } else {
//            Toast.makeText(context, "找不到SD卡，容易造成数据丢失", Toast.LENGTH_LONG).show();
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
//                Toast.makeText(context, "没找到SD卡，无法保存图片", Toast.LENGTH_SHORT).show();
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
//                    Picasso.with(activity.getBaseContext()).invalidate(file);
                    Picasso.with(activity).load(file)
                            .error(R.drawable.ic_user_icon)
                            .placeholder(R.drawable.ic_user_icon)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(view);
                }
            });

        } else {

            new Utils().uploadUserImage(activity, user_image, file, "userImage", user_number);
//            Intent intent = new Intent(activity, ImageDisplayActivity.class);
//            intent.putExtra("image_uri",ImagePath);
//            activity.startActivity(intent);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {//http://192.168.43.196/UBasketball/     "http://wyuzww.nat123.net/UBasketball/userImage/13612250853.jpg"
//                    Picasso.with(activity).invalidate(ImagePath);
                    Picasso.with(activity).load(ImagePath)
                            .error(R.drawable.ic_user_icon)
                            .placeholder(R.drawable.ic_user_icon)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(view);
                }
            });
        }
    }

    /**
     * 获取网络图片
     *
     * @param imageUrl 图片网络地址
     * @return Bitmap 返回位图
     */
    public void uploadUserImage(final Context context, String imageUrl, final File temp_file, final String type, final String user_number) {

        HttpClient httpClient = new HttpClient();
        httpClient.request_getImage(imageUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bitmap bitmap = null;
                InputStream inputStream = response.body().byteStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出//
//                BitmapFactory.decodeStream(inputStream,null, options);
                int height = options.outHeight;
                int width = options.outWidth;
                int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                int minLen = Math.min(height, width); // 原图的最小边长
                if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                    float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
                    inSampleSize = (int) ratio;
                }
                options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                bitmap = BitmapFactory.decodeStream(inputStream, null, options); // 解码文件
                inputStream.close();


                //创建文件
                File file = createImageFile(context, temp_file, type, user_number);
                //定义文件输出流
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                //将bitmap存储为jpg格式的图片
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                }
                fileOutputStream.flush();//刷新文件流
                fileOutputStream.close();


            }
        });
    }

    public File compressImage(File file) {

        try {
            Bitmap bitmap = null;
            InputStream inputStream = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出//
//                BitmapFactory.decodeStream(inputStream,null, options);
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
            int minLen = Math.min(height, width); // 原图的最小边长
            if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
                inSampleSize = (int) ratio;
            }
            options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
            options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
            bitmap = BitmapFactory.decodeStream(inputStream, null, options); // 解码文件
            inputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //将bitmap存储为jpg格式的图片
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            }
            fileOutputStream.flush();//刷新文件流
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setFullScreen(Window window) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(decorView.getResources().getColor(R.color.textSelectColor));
        }
    }

    public void setFullScreenCustomColor(Window window, Integer color_id) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(decorView.getResources().getColor(color_id));
        }
    }





}
