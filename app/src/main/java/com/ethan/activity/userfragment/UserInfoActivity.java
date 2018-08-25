package com.ethan.activity.userfragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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

import com.ethan.R;
import com.ethan.util.control.ActivityCollector;
import com.ethan.util.network.HttpClient;

import java.util.Calendar;
import java.util.TimeZone;

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
        user_exit_BT = (Button) findViewById(R.id.user_exit);
        edit_change_TV = (TextView) findViewById(R.id.to_change_id);
        user_image_IV = (ImageView) findViewById(R.id.user_image);
        user_name_head_TV = (TextView) findViewById(R.id.user_name_head);
        user_name_ET = (EditText) findViewById(R.id.user_name);
        edit_save_TV = (TextView) findViewById(R.id.to_save_id);
        back_BT = (Button) findViewById(R.id.back_button);
        user_sex_TV = (TextView) findViewById(R.id.user_sex);
        user_birth_TV = (TextView) findViewById(R.id.user_birth);
        user_signature_ET = (EditText) findViewById(R.id.user_signature);

        user_info_preferences = this.getSharedPreferences("UserInfo", 0);
        user_exit_BT.setOnClickListener(this);
        edit_change_TV.setOnClickListener(this);
        //user_image_IV.setOnClickListener(this);
        edit_save_TV.setOnClickListener(this);
        back_BT.setOnClickListener(this);
        //user_sex_TV.setOnClickListener(this);
//        user_birth_TV.setOnClickListener(this);
    }

    private void initUser() {
        user_info_preferences = this.getSharedPreferences("UserInfo", 0);
        //user_image_IV.setImageBitmap();//头像
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
            case R.id.user_image:
                Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
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
            user_image_IV.setClickable(true);
            user_image_IV.setOnClickListener(this);
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
//                final ProgressDialog login_Dialog = new ProgressDialog(this);
////                login_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////                login_Dialog.setCancelable(false);
////                login_Dialog.setCanceledOnTouchOutside(false);
////                login_Dialog.setMessage("正在保存...");
////                login_Dialog.show();
////
////                String url = "http://wyuzww.nat123.net/UBasketball/";
////                HttpClient httpClient = new HttpClient();

            }
            Toast.makeText(this, "保存到服务器", Toast.LENGTH_SHORT).show();
        }

        {//不可点击控件
            user_image_IV.setClickable(false);
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

}
