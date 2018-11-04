package com.ethan.activity.userfragment.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber_ET;
    private EditText verificationCode_ET;
    private Button sendCode_BT;
    private EditText passwordNumber_ET;
    private Button showPassword_BT;
    private Button registerOk_BT;
    private EventHandler eventHandler; //事件接收器
    private TimeCount timeCount;//计时器
    private Handler handler;
    private Button backButton;
    private boolean flag = true;
    private boolean showPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        new Utils().setFullScreen(getWindow());
        init();
        initEventHandler();

        ActivityCollector.addActivity(this);
    }

    private void init() {
        phoneNumber_ET = (EditText) findViewById(R.id.phone_number_id);
        verificationCode_ET = (EditText) findViewById(R.id.verification_code_id);
        sendCode_BT = (Button) findViewById(R.id.send_code_id);
        passwordNumber_ET = (EditText) findViewById(R.id.password_number_id);
        registerOk_BT = (Button) findViewById(R.id.reset_ok_id);
        backButton = (Button) findViewById(R.id.back_button);
        showPassword_BT = (Button) findViewById(R.id.show_password_id);

        timeCount = new TimeCount(60000, 1000);
        sendCode_BT.setOnClickListener(this);
        registerOk_BT.setOnClickListener(this);
        backButton.setOnClickListener(this);
        showPassword_BT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_code_id:
                SendVerificationCode();
                break;
            case R.id.reset_ok_id:
                Register();
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.show_password_id:
                ChangeShowPassword();
                break;
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
                        //Toast.makeText(RegisterActivity.this, "验证验证码成功", Toast.LENGTH_SHORT).show();

                        toRegister(phoneNumber_ET.getText().toString().trim(), passwordNumber_ET.getText().toString().trim());


//                        Intent intent = new Intent();
//                   //     Bundle bundle = new Bundle();
//                        intent.putExtra("phone", phoneNumber_ET.getText().toString().trim());
//                        intent.putExtra("password", passwordNumber_ET.getText().toString().trim());
//                        setResult(RESULT_OK,intent);
//                        finish();
                        //  bundle.putString("phone", phoneNumber_ET.getText().toString().trim());
                        //   bundle.putString("password", passwordNumber_ET.getText().toString().trim());
                        //   intent.putExtras(bundle);
                        //intent.putExtra("password",passwordNumber_ET.getText().toString().trim());
                        //    startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                        Log.e("RegisterActivity", "获取验证码成功");
                        Toast.makeText(RegisterActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                        Toast.makeText(RegisterActivity.this, "返回支持发送验证码的国家列表", Toast.LENGTH_SHORT).show();
                        Log.e("RegisterActivity", "返回支持发送验证码的国家列表");
                    }
                } else {
                    if (flag) {
                        Toast.makeText(RegisterActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                    //((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    private void SendVerificationCode() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), 0);

        //SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
        if (!phoneNumber_ET.getText().toString().trim().equals("")) {
            if (checkTel(phoneNumber_ET.getText().toString().trim())) {
                flag = true;
                SMSSDK.getVerificationCode("+86", phoneNumber_ET.getText().toString());//获取验证码
                timeCount.start();
                Toast.makeText(RegisterActivity.this, "已发送验证码", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    private void Register() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), 0);

        if (!phoneNumber_ET.getText().toString().trim().equals("")) {
            if (checkTel(phoneNumber_ET.getText().toString().trim())) {
                if (!verificationCode_ET.getText().toString().trim().equals("")) {
                    if (!passwordNumber_ET.getText().toString().trim().equals("")) {
                        SMSSDK.submitVerificationCode("+86", phoneNumber_ET.getText().toString().trim(), verificationCode_ET.getText().toString().trim());//提交验证
                        flag = false;
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
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

    private void ChangeShowPassword() {
        if (showPassword) {
            showPassword_BT.setBackground(getResources().getDrawable(R.drawable.eyeclose));
            passwordNumber_ET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordNumber_ET.setSelection(passwordNumber_ET.getText().toString().trim().length());
            showPassword = !showPassword;
        } else {
            showPassword_BT.setBackground(getResources().getDrawable(R.drawable.eyeopen));
            passwordNumber_ET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordNumber_ET.setSelection(passwordNumber_ET.getText().toString().trim().length());
            showPassword = !showPassword;
        }
    }

    private void toRegister(final String user_number, final String user_password) {

        final ProgressDialog register_Dialog = new ProgressDialog(this);
        register_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        register_Dialog.setCancelable(false);
        register_Dialog.setCanceledOnTouchOutside(false);
        register_Dialog.setMessage("正在注册...");
        register_Dialog.show();

//        String url = "http://wyuzww.nat123.net/UBasketball/register";
        String url = "Register";
        FormBody formBody = new FormBody.Builder()
                .add("user_number", user_number)
                .add("user_password", new Utils().md5Password(user_password))
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
                            Toast.makeText(RegisterActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                            register_Dialog.dismiss();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            register_Dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                            register_Dialog.dismiss();
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
                                Toast.makeText(RegisterActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                register_Dialog.dismiss();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                register_Dialog.dismiss();
                                if (code == 0) {
                                    Intent intent = new Intent();
                                    intent.putExtra("phone", user_number);
                                    intent.putExtra("password", user_password);
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
                            Toast.makeText(RegisterActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                            register_Dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

}