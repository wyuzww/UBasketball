package com.ethan.activity.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.control.ActivityCollector;
import com.ethan.util.network.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginByMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber_ET;
    private EditText verificationCode_ET;
    private Button sendCode_BT;
    private Button okLogin_BT;
    private EventHandler eventHandler; //事件接收器
    private LoginByMessageActivity.TimeCount timeCount;//计时器
    private Handler handler;
    //    private SharedPreferences user_info_preferences;
//    private SharedPreferences.Editor editor;
    private boolean flag = true;


    private Button back_BT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bymessage);

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        init();

        initEventHandler();

        ActivityCollector.addActivity(this);
    }

    private void init() {
        phoneNumber_ET = (EditText) findViewById(R.id.phone_number_id);
        verificationCode_ET = (EditText) findViewById(R.id.verification_code_id);
        sendCode_BT = (Button) findViewById(R.id.send_code_id);
        okLogin_BT = (Button) findViewById(R.id.login_ok_id);
        back_BT = (Button) findViewById(R.id.back_button);

//        user_info_preferences = this.getSharedPreferences("UserInfo",0);
        timeCount = new TimeCount(60000, 1000);
        sendCode_BT.setOnClickListener(this);
        okLogin_BT.setOnClickListener(this);
        back_BT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.send_code_id:
                sendVerificationCode();
                break;
            case R.id.login_ok_id:
                login();
                break;
        }
    }

    /**
     * 初始化事件接收器
     */
    private void initEventHandler() {
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                handler.sendMessage(message);
            }
        };

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //验证验证码成功
                        Log.e("RegisterActivity", "验证验证码成功");
                        //Toast.makeText(LoginByMessageActivity.this, "验证验证码成功", Toast.LENGTH_SHORT).show();
                        loginByMessage(phoneNumber_ET.getText().toString().trim());

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                        Log.e("RegisterActivity", "获取验证码成功");
                        Toast.makeText(LoginByMessageActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                        //Toast.makeText(LoginByMessageActivity.this, "返回支持发送验证码的国家列表", Toast.LENGTH_SHORT).show();
                        Log.e("RegisterActivity", "返回支持发送验证码的国家列表");
                    }
                } else {
                    if (flag) {
                        Toast.makeText(LoginByMessageActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginByMessageActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    private void login() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(LoginByMessageActivity.this.getCurrentFocus().getWindowToken(), 0);

        if (!phoneNumber_ET.getText().toString().trim().equals("")) {
            if (checkTel(phoneNumber_ET.getText().toString().trim())) {
                if (!verificationCode_ET.getText().toString().trim().equals("")) {
                    SMSSDK.submitVerificationCode("+86", phoneNumber_ET.getText().toString().trim(), verificationCode_ET.getText().toString().trim());//提交验证
                    flag = false;
                } else {
                    Toast.makeText(LoginByMessageActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginByMessageActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginByMessageActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(LoginByMessageActivity.this.getCurrentFocus().getWindowToken(), 0);

        //SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
        if (!phoneNumber_ET.getText().toString().trim().equals("")) {
            if (checkTel(phoneNumber_ET.getText().toString().trim())) {
                flag = true;
                SMSSDK.getVerificationCode("+86", phoneNumber_ET.getText().toString());//获取验证码
                timeCount.start();
                Toast.makeText(LoginByMessageActivity.this, "已发送验证码", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginByMessageActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginByMessageActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 正则匹配手机号码
     *
     * @param tel
     * @return
     */
    public boolean checkTel(String tel) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
        ActivityCollector.removeActivity(this);
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            sendCode_BT.setClickable(false);
            sendCode_BT.setText(l / 1000 + "S");
        }

        @Override
        public void onFinish() {
            sendCode_BT.setClickable(true);
            sendCode_BT.setText("验证码");
        }
    }


    private void loginByMessage(final String user_number) {

        final ProgressDialog login_Dialog = new ProgressDialog(this);
        login_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        login_Dialog.setCancelable(false);
        login_Dialog.setCanceledOnTouchOutside(false);
        login_Dialog.setMessage("正在登录...");
        login_Dialog.show();

//        String url = "http://wyuzww.nat123.net/UBasketball/register";
        String url = "LoginByMessage";
        FormBody formBody = new FormBody.Builder()
                .add("user_number", user_number)
                .build();
        // String url = "http://wyuzww.nat123.net/UBasketball/register?user_number=" + user_number + "&user_password=" + user_password;
        HttpClient httpClient = new HttpClient();

        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginByMessageActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                            login_Dialog.dismiss();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginByMessageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            login_Dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginByMessageActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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

                    //解析json
                    final User user = JSONObject.parseObject(responseText, User.class);
                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginByMessageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginByMessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                                if (code == 0) {
                                    new Utils().saveUser(LoginByMessageActivity.this, user);
                                    //saveUser(user);
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);

                                    finish();

                                }
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginByMessageActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                            login_Dialog.dismiss();
                        }
                    });
                }
            }
        });
    }
//
//    private void saveUser(User user) {
//        editor = user_info_preferences.edit();
//        editor.putBoolean("isLogined",true);
//        editor.putInt("user_id",user.getUser_id());
//        editor.putString("user_name",user.getUser_name());
//        editor.putString("user_number",user.getUser_number());
//        editor.putString("user_password",user.getUser_password());
//        editor.putString("user_sex",user.getUser_sex());
//        editor.putString("user_birth",user.getUser_birth());
//        editor.putString("user_signature",user.getUser_signature());
//        editor.putString("user_token",user.getUser_token());
//        editor.apply();
//    }


}
