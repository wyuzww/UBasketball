package ethan.com.ubasketball;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber_ET;
    private EditText verificationCode_ET;
    private Button sendCode_BT;
    private EditText passwordNumber_ET;
    private Button registerOk_BT;
    private EventHandler eventHandler; //事件接收器
    private TimeCount timeCount;//计时器
    private Handler handler;
    private Button backButton;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();
        initEventHandler();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_code_id:
//                SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
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
                break;
            case R.id.register_ok_id:
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
                break;
            case R.id.back_button:
                startActivity(new Intent(RegisterActivity.this, LoginByPasswordActivity.class));
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
    }


    private void init() {
        phoneNumber_ET = (EditText) findViewById(R.id.phone_number_id);
        verificationCode_ET = (EditText) findViewById(R.id.verification_code_id);
        sendCode_BT = (Button) findViewById(R.id.send_code_id);
        passwordNumber_ET = (EditText) findViewById(R.id.password_number_id);
        registerOk_BT = (Button) findViewById(R.id.register_ok_id);
        backButton = (Button) findViewById(R.id.back_button);
        timeCount = new TimeCount(60000, 1000);
        sendCode_BT.setOnClickListener(this);
        registerOk_BT.setOnClickListener(this);
        backButton.setOnClickListener(this);
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
                        Toast.makeText(RegisterActivity.this, "验证验证码成功", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));//页面跳转
                        Intent intent = new Intent(RegisterActivity.this, LoginByPasswordActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phoneNumber_ET.getText().toString().trim());
                        bundle.putString("password", passwordNumber_ET.getText().toString().trim());
                        intent.putExtras(bundle);
                        //intent.putExtra("password",passwordNumber_ET.getText().toString().trim());
                        startActivity(intent);
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
            sendCode_BT.setText(l / 1000 + "s");
        }

        @Override
        public void onFinish() {
            sendCode_BT.setClickable(true);
            sendCode_BT.setText("验证码");
        }
    }


}