package com.ethan.activity.userfragment.user;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.other.ImageDisplayActivity;
import com.ethan.activity.userfragment.login.LoginByPasswordActivity;
import com.ethan.util.Utils;
import com.ethan.util.control.ActivityCollector;
import com.ethan.util.network.HttpClient;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.squareup.picasso.MemoryPolicy;
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

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
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


    private TakePhoto takePhoto;
    private CropOptions cropOptions;
    private InvokeParam invokeParam;


    private File mImageFile = null;
    private Uri photoUri = null;
    private String user_number = null;
    private String user_image = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        new Utils().setFullScreen(getWindow());

        bindView();

        initTakePhoto();

        initUser();

        ActivityCollector.addActivity(this);
    }

    private void bindView() {

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

    private void initTakePhoto() {
        cropOptions = new CropOptions.Builder().setOutputX(1).setOutputX(1).setWithOwnCrop(true).create();
    }

    private void initUser() {
        user_info_preferences = this.getSharedPreferences("UserInfo", 0);
        user_number = user_info_preferences.getString("user_number", "user_temp");
        user_image = user_info_preferences.getString("user_image", "userImage/" + user_number + ".jpg");

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
//                final File file = new File("/sdcard/UBasketball/" + user_number + "/user_image/" + user_number + ".jpg");
//                file.delete();
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

//                File file = new File(this.getExternalFilesDir("tempImage"), user_number + ".jpg");
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
                if (mImageFile != null) {
                    body.addFormDataPart("userImage", mImageFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), new Utils().compressImage(this, mImageFile, mImageFile)));

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

    private void clickImage() {
        if (edit_change_TV.getVisibility() == View.VISIBLE) {
            seeImage();
        } else {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            changeImage();
        }
    }

    private void seeImage() {
        File file = new File(getExternalFilesDir("userImage"), user_number + ".jpg");

        if (file.exists()) {
            Intent intent = new Intent(UserInfoActivity.this, ImageDisplayActivity.class);
            intent.putExtra("image_uri", String.valueOf(Uri.fromFile(file)));
            startActivity(intent);
        } else {

            new Utils().uploadUserImage(this, user_image, file, "userImage", user_number);

            String imagePath = new HttpClient().getURL() + user_image;
//            Intent intent = new Intent(UserInfoActivity.this, ImageDisplayActivity.class);
//            intent.putExtra("image_uri", imagePath);
//            startActivity(intent);
            new Utils().toShowImage(this, imagePath);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void takeSuccess(TResult result) {

//        String path = result.getImage().getOriginalPath();
//
//        photoUri = Uri.fromFile(new File(path));

        Picasso.with(UserInfoActivity.this).load(photoUri).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(user_image_IV);

    }

    @Override
    public void takeFail(TResult result, String msg) {
        mImageFile = null;
        Toast.makeText(UserInfoActivity.this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        mImageFile = null;
        Toast.makeText(UserInfoActivity.this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void changeImage() {
        new AlertDialog.Builder(UserInfoActivity.this)
                .setTitle("选择照片")
                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mImageFile = new Utils().createImageFile(UserInfoActivity.this, mImageFile, "tempImage", user_number);
//                        file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
//                        if (!file.getParentFile().exists()) { file.getParentFile().mkdirs(); }
                        if (!mImageFile.isFile()) {
                            Toast.makeText(UserInfoActivity.this, "没找到SD卡，无法使用该功能", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        photoUri = Uri.fromFile(mImageFile);
                        switch (which) {
                            case 0:
                                //拍照并裁剪
                                takePhoto.onPickFromCaptureWithCrop(photoUri, cropOptions);
                                break;
                            case 1:
                                //从照片选择并裁剪
                                takePhoto.onPickFromGalleryWithCrop(photoUri, cropOptions);
                                break;
                            default:
                                break;
                        }
                    }
                }).create().show();
    }
}
