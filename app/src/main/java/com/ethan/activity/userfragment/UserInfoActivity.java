package com.ethan.activity.userfragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.login.LoginByPasswordActivity;
import com.ethan.util.Utils;
import com.ethan.util.control.ActivityCollector;
import com.ethan.util.network.HttpClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button user_exit_BT;
    private TextView edit_change_TV;
    private ImageView user_image_IV;
    private TextView user_name_head_TV;
    private EditText user_name_ET;
    private TextView edit_save_TV;
    private TextView user_sex_TV;
    private Button back_BT;
    private TextView user_birth_TV;
    private EditText user_signature_ET;
    private SharedPreferences user_info_preferences;
    private SharedPreferences.Editor editor;

    private File mImageFile = null;
    private Uri photoUri = null;
    private String user_number = null;
    private String user_image = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();

        initUser();

        ActivityCollector.addActivity(this);
    }

    private void init() {

        user_exit_BT = findViewById(R.id.user_exit);
        edit_change_TV = findViewById(R.id.to_change_id);
        user_image_IV = findViewById(R.id.user_image_id);
        user_name_head_TV = findViewById(R.id.user_name_head);
        user_name_ET = findViewById(R.id.user_name);
        edit_save_TV = findViewById(R.id.to_save_id);
        back_BT = findViewById(R.id.back_button);
        user_sex_TV = findViewById(R.id.user_sex);
        user_birth_TV = findViewById(R.id.user_birth);
        user_signature_ET = findViewById(R.id.user_signature);

        //user_info_preferences = this.getSharedPreferences("UserInfo", 0);
        user_exit_BT.setOnClickListener(this);
        edit_change_TV.setOnClickListener(this);
        user_image_IV.setOnClickListener(this);
        edit_save_TV.setOnClickListener(this);
        back_BT.setOnClickListener(this);
        //user_sex_TV.setOnClickListener(this);
//        user_birth_TV.setOnClickListener(this);
    }

    private void initUser() {
        user_info_preferences = this.getSharedPreferences("UserInfo", 0);
        user_number = user_info_preferences.getString("user_number", "user_temp");
        user_image = user_info_preferences.getString("user_image", "userImage/" + user_number + ".jpg");
//        final File file = new File("/sdcard/UBasketball/"+user_number+"/user_image/"+user_number+".jpg");

//        final File file = new File(getExternalFilesDir("userImage"), user_number + ".jpg");
//
//        //user_image_IV.setImageBitmap();//头像
//        if (file.exists()) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Picasso.with(UserInfoActivity.this).invalidate(file);
//                    Picasso.with(UserInfoActivity.this).load(file)
//                            .into(user_image_IV);
//                }
//            });
//
//        } else {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {//http://192.168.43.196/UBasketball/     "http://wyuzww.nat123.net/UBasketball/userImage/13612250853.jpg"
//                    Picasso.with(UserInfoActivity.this).invalidate("http://192.168.43.196/UBasketball/userImage/13612250853.jpg");
//                    Picasso.with(UserInfoActivity.this).load("http://192.168.43.196/UBasketball/userImage/13612250853.jpg")
//                            .error(R.mipmap.ic_logo)
//                            .placeholder(R.mipmap.ic_logo)
//                            .into(user_image_IV);
//                }
//            });
//        }
        new Utils().findUserImage(user_image, user_number, this, user_image_IV);

        user_name_head_TV.setText(user_info_preferences.getString("user_name", ""));
        user_name_ET.setText(user_info_preferences.getString("user_name", ""));
        user_sex_TV.setText(user_info_preferences.getString("user_sex", ""));
        user_birth_TV.setText(user_info_preferences.getString("user_birth", ""));
        user_signature_ET.setText(user_info_preferences.getString("user_signature", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_exit:
                userExit();
                break;
            case R.id.to_change_id:
                editChange();
                break;
            case R.id.user_image_id:
                clickImage();
                break;
            case R.id.to_save_id:
                saveChange(1);
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.user_sex:
                changeSex();
                break;
            case R.id.user_birth:
                changeBirth();
                break;
        }
    }

    private void userExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final File file = new File("/sdcard/UBasketball/" + user_number + "/user_image/" + user_number + ".jpg");
                file.delete();
                editor = user_info_preferences.edit();
                editor.clear();
                editor.putBoolean("isLogined", false);
                editor.apply();
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

    private void editChange() {
        {//可点击控件
            //头像
//            user_image_IV.setClickable(true);
//            user_image_IV.setOnClickListener(this);
            //性别
            user_sex_TV.setClickable(true);
            user_sex_TV.setOnClickListener(this);
            user_sex_TV.setEnabled(true);

            //生日
            user_birth_TV.setClickable(true);
            user_birth_TV.setOnClickListener(this);
            user_birth_TV.setEnabled(true);
        }
        {
            //头像昵称处
            user_name_head_TV.setText("点击更改头像");
        }
        {//可编辑控件
            //昵称
            user_name_ET.setEnabled(true);
            user_name_ET.setSelection(user_name_ET.getText().toString().trim().length());
            //个性签名
            user_signature_ET.setEnabled(true);
            user_signature_ET.setSelection(user_signature_ET.getText().toString().length());

        }
        {//退出登录按钮不可见
            user_exit_BT.setVisibility(View.GONE);
        }
        {//编辑和保存的可视情况
            edit_change_TV.setVisibility(View.GONE);
            edit_save_TV.setVisibility(View.VISIBLE);
        }
    }


    private void saveChange(int flag) {
        //flag=1为保存，其他为放弃保存
        if (flag == 1) {
            {//请求网络，修改token为****的用户个人信息
                final ProgressDialog login_Dialog = new ProgressDialog(this);
                login_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                login_Dialog.setCancelable(false);
                login_Dialog.setCanceledOnTouchOutside(false);
                login_Dialog.setMessage("正在保存...");
                login_Dialog.show();

                File file = new File(this.getExternalFilesDir("tempImage"), user_number + ".jpg");
                String url = "UpdateUserInfo";
//                FormBody formBody = new FormBody.Builder()
//                        .add("user_token", user_info_preferences.getString("user_token", ""))
//                        .add("user_name", user_name_ET.getText().toString())
//                        .add("user_sex", user_sex_TV.getText().toString())
//                        .add("user_birth", user_birth_TV.getText().toString())
//                        .add("user_signature", user_signature_ET.getText().toString())
//                        .build();

                MultipartBody.Builder body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if (file.exists() && mImageFile != null) {
                    body.addFormDataPart("userImage", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), mImageFile));

                }
                //.addPart(Headers.of("Content-Disposition","form-data;name=\"user_token\""),RequestBody.create(null,user_info_preferences.getString("user_token", "")))
                body.addFormDataPart("user_token", user_info_preferences.getString("user_token", ""))
                        .addFormDataPart("user_name", user_name_ET.getText().toString())
                        .addFormDataPart("user_sex", user_sex_TV.getText().toString())
                        .addFormDataPart("user_birth", user_birth_TV.getText().toString())
                        .addFormDataPart("user_signature", user_signature_ET.getText().toString());
//                if (file.exists() && mImageFile != null) {
//                    body.addFormDataPart("userImage", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), mImageFile));
//                }
                MultipartBody multipartBody = body.build();

                HttpClient httpClient = new HttpClient();
                httpClient.requestImage_Post(url, multipartBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        } else if (e instanceof ConnectException) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        }
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            final String responseText = response.body().string();

                            final int code = JSONObject.parseObject(responseText).getInteger("code");
                            final String msg = JSONObject.parseObject(responseText).getString("msg");

                            if (msg.isEmpty() || msg.equals("")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserInfoActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                        login_Dialog.dismiss();
                                    }
                                });
                            } else if (code == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        login_Dialog.dismiss();
                                        new AlertDialog.Builder(UserInfoActivity.this)
                                                .setMessage(msg + ",请重新登录")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(UserInfoActivity.this, LoginByPasswordActivity.class));
                                                        finish();
                                                    }
                                                }).show();
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        login_Dialog.dismiss();
                                        if (code == 0) {
                                            if (mImageFile != null) {
                                                new Utils().SaveImage(UserInfoActivity.this, mImageFile, "userImage", user_number);
                                                mImageFile = null;
                                            }
                                            editor = user_info_preferences.edit();
                                            editor.putString("user_name", user_name_ET.getText().toString());
                                            editor.putString("user_sex", user_sex_TV.getText().toString());
                                            editor.putString("user_birth", user_birth_TV.getText().toString());
                                            editor.putString("user_signature", user_signature_ET.getText().toString());
                                            editor.apply();
                                        }
                                    }
                                });
                            }

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UserInfoActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }

        {//不可点击控件
//            user_image_IV.setClickable(false);
            user_sex_TV.setClickable(false);
            user_birth_TV.setClickable(false);

        }
        {//头像显示信息
            user_name_head_TV.setText(user_name_ET.getText());
        }
        {//不可编辑控件
            user_name_ET.setEnabled(false);
            user_signature_ET.setEnabled(false);
            user_sex_TV.setEnabled(false);
            user_birth_TV.setEnabled(false);
        }
        {//退出登录按钮可见
            user_exit_BT.setVisibility(View.VISIBLE);
        }
        {//编辑和保存的可视情况
            edit_change_TV.setVisibility(View.VISIBLE);
            edit_save_TV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (edit_change_TV.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否放弃修改");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initUser();
                    mImageFile = null;
                    saveChange(2);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
    }

    private void changeImage() {
        new AlertDialog.Builder(UserInfoActivity.this)
                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mImageFile = new Utils().createImageFile(UserInfoActivity.this, mImageFile, "tempImage", user_number);
                            if (!mImageFile.isFile()) {
                                Toast.makeText(UserInfoActivity.this, "没找到SD卡，无法使用该功能", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent Mintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            //添加判断SDK,Android7.0以上需要用FileProvider和addFlag
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                photoUri = FileProvider.getUriForFile(
                                        UserInfoActivity.this,
                                        "com.ethan",
                                        mImageFile);
                                Mintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                photoUri = Uri.fromFile(mImageFile);
                            }

                            Mintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(Mintent, 102);

                        } else {
                            mImageFile = new Utils().createImageFile(UserInfoActivity.this, mImageFile, "tempImage", user_number);
                            if (!mImageFile.isFile()) {
                                Toast.makeText(UserInfoActivity.this, "没找到SD卡，无法使用该功能", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            Intent Gintent = new Intent(Intent.ACTION_PICK,
//                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用系统图库
                            Intent Gintent = new Intent(Intent.ACTION_PICK);
                            Gintent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(Gintent, 101);
                        }
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            changeImage();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
//                Toast.makeText(this,"请进行裁剪",Toast.LENGTH_SHORT).show();
                startPhotoZoom(uri);// 裁剪图片
            } else {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            Uri uri = photoUri;
//            Toast.makeText(this,"请进行裁剪",Toast.LENGTH_SHORT).show();
            startPhotoZoom(uri);// 裁剪图片
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(UserInfoActivity.this).invalidate(mImageFile);//清除缓存
                    Picasso.with(UserInfoActivity.this).load(mImageFile).into(user_image_IV);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(UserInfoActivity.this).invalidate(mImageFile);//清除缓存
                    Picasso.with(UserInfoActivity.this).load(new File(getExternalFilesDir("userImage"), user_number + ".jpg")).into(user_image_IV);
                }
            });
            mImageFile = null;
        }
    }

    private void changeSex() {
        final String[] sex = {"男", "女"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user_sex_TV.setText(sex[which]);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void changeBirth() {
        //Toast.makeText(this,"请选择日期",Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                user_birth_TV.setText(String.format("%d 年 %d 月 %d 日", year, month + 1, dayOfMonth));
            }
        }, year, month, day).show();
    }

    // 裁剪方法
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用系统裁剪器
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        //intent.putExtra("return-data", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true);
        //添加判断SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(intent, 100);
    }

    // 存储方法
//    private void SaveImage(File temp_file,String type,String user_number) {
////        Toast.makeText(this,"save1",Toast.LENGTH_SHORT).show();
//        File userImage = null;
//        userImage = createImageFile(userImage,type,user_number);
//        FileInputStream inputStream = null;
//        FileOutputStream outputStream = null;
//
//        byte[] bytes = new byte[1024];
//        int lenth=0;
//        try {
//            if (!userImage.isFile()) {
//                return;
//            }
//  //          Toast.makeText(this,"save2",Toast.LENGTH_SHORT).show();
//            inputStream = new FileInputStream(temp_file);
//            outputStream = new FileOutputStream(userImage);
//            while ((lenth=inputStream.read(bytes)) > 0) {
//                outputStream.write(bytes,0,lenth);
//            }
//    //        Toast.makeText(this,"save3",Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    private void applyChangeImage() {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                changeImage();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            changeImage();
        }
    }

//    private File createImageFile(File mfile,String type,String user_number) {
//        if (isSdcard()) {
//            File file = getExternalFilesDir(type);
//            mfile = new File(file.getPath(), user_number+".jpg");
//            try {
//                if (mfile.exists()) {
//                    mfile.delete();
//                    //getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DATA + "=?",new String[]{mImageFile.getPath()});
//
//                }
//                mfile.createNewFile();
//                return mfile;
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "出错啦", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//
//        } else {
//            Toast.makeText(this, "找不到SD卡，容易造成数据丢失", Toast.LENGTH_LONG).show();
//            return null;
//        }
////        File file = new File("/sdcard/UBasketball/"+user_number+"/temp_image/");
////        file.mkdirs();//创建目录
////        mImageFile = new File(file.getPath(), user_number+".jpg");
////        try {
////            if (mImageFile.exists()) {
////                mImageFile.delete();
////                //getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DATA + "=?",new String[]{mImageFile.getPath()});
////
////            }
//////            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//////            scanIntent.setData(Uri.fromFile(new File(mImageFile.getPath())));
//////            this.sendBroadcast(scanIntent);
////            mImageFile.createNewFile();
////        } catch (IOException e) {
////            e.printStackTrace();
////            Toast.makeText(this, "出错啦", Toast.LENGTH_SHORT).show();
////        }
//    }

//    private boolean isSdcard() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    private void clickImage() {
        if (edit_change_TV.getVisibility() == View.VISIBLE) {
            seeImage();
        } else {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            applyChangeImage();
        }
    }

    private void seeImage() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View userImageDialog = inflater.inflate(R.layout.user_image_dialog, null); // 加载自定义的布局文件
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        ImageView userImage_dialog = (ImageView) userImageDialog.findViewById(R.id.large_userImage);
        // 这个是加载网络图片的，可以是自己的图片设置方法
        new Utils().findUserImage(user_image, user_number, this, userImage_dialog);
//        Picasso.with(this).invalidate("http://192.168.43.196/UBasketball/userImage/13612250853.jpg");
//        Picasso.with(this).load("http://192.168.43.196/UBasketball/userImage/13612250853.jpg").placeholder(R.mipmap.ic_logo).error(R.mipmap.ic_logo).into(userImage_dialog);
        dialog.setView(userImageDialog); // 自定义dialog
        dialog.show();
// 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        userImageDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }
}
