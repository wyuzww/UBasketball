package com.ethan.activity.userfragment.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.other.ResetPasswordActivity;
import com.ethan.activity.userfragment.register.RegisterActivity;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.control.ActivityCollector;
import com.ethan.util.network.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginByPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber_ET;
    private EditText passwordNumber_ET;
    private TextView loginByMessage_TV;
    private TextView forget_password_TV;
    private TextView toRegister_TV;
    private Button okLogin_BT;
    private Button showPassword_BT;
    private Button back_BT;
    //    private SharedPreferences user_info_preferences;
    private SharedPreferences user_ZH_preferences;
    private SharedPreferences.Editor editor;
    private CheckBox remember_password_CB;

    private boolean showPassword = false;
    private boolean isRemember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bypassword);

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.textSelectColor));
//        }
        new Utils().setFullScreen(getWindow());

        bindView();


        loadZH();

        //      getBundle();

        ActivityCollector.addActivity(this);

    }

    private void bindView() {
        loginByMessage_TV = (TextView) findViewById(R.id.login_bymessage_id);
        forget_password_TV = (TextView) findViewById(R.id.forget_password_id);
        toRegister_TV = (TextView) findViewById(R.id.to_register_id);
        okLogin_BT = (Button) findViewById(R.id.login_ok_id);
        loginByMessage_TV.setOnClickListener(this);
        phoneNumber_ET = (EditText) findViewById(R.id.phone_number_id);
        passwordNumber_ET = (EditText) findViewById(R.id.password_number_id);
        showPassword_BT = (Button) findViewById(R.id.login_show_password_id);
        back_BT = (Button) findViewById(R.id.back_button);
//        user_info_preferences = this.getSharedPreferences("UserInfo",0);
        user_ZH_preferences = this.getSharedPreferences("UserZH", 0);
        remember_password_CB = (CheckBox) findViewById(R.id.check_remember_password);

        toRegister_TV.setOnClickListener(this);
        forget_password_TV.setOnClickListener(this);
        okLogin_BT.setOnClickListener(this);
        showPassword_BT.setOnClickListener(this);
        back_BT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bymessage_id:
                startActivityForResult(new Intent(LoginByPasswordActivity.this, LoginByMessageActivity.class), 1);
                break;
            case R.id.forget_password_id:
                startActivity(new Intent(LoginByPasswordActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.to_register_id:
                //带返回数据的Intent
                startActivityForResult(new Intent(LoginByPasswordActivity.this, RegisterActivity.class), 0);
                break;
            case R.id.login_ok_id:
                login();
                break;
            case R.id.login_show_password_id:
                changeShowPassword();
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    private void login() {
        //强制收起键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(LoginByPasswordActivity.this.getCurrentFocus().getWindowToken(), 0);
//        final ProgressBar login_Dialog = new ProgressBar(this);
//        login_Dialog.setVisibility(View.VISIBLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (true) {
            editor = user_ZH_preferences.edit();
            if (remember_password_CB.isChecked()) {

                editor.putBoolean("remember_password", true);
                editor.putString("phoneNumber", phoneNumber_ET.getText().toString().trim());
                editor.putString("password", passwordNumber_ET.getText().toString().trim());
            } else {
                editor.clear();
                editor.putString("phoneNumber", phoneNumber_ET.getText().toString().trim());
            }
            editor.apply();
        }


        if (!phoneNumber_ET.getText().toString().trim().equals("")) {
            if (!passwordNumber_ET.getText().toString().trim().equals("")) {

                final ProgressDialog login_Dialog = new ProgressDialog(this);
                login_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                login_Dialog.setCancelable(false);
                login_Dialog.setCanceledOnTouchOutside(false);
                login_Dialog.setMessage("正在登录...");
                login_Dialog.show();

                //   String url = "http://wyuzww.nat123.net/UBasketball/login";
                String url = "LoginByPassword";
                FormBody formBody = new FormBody.Builder()
                        .add("user_number", phoneNumber_ET.getText().toString().trim())
                        .add("user_password", new Utils().md5Password(passwordNumber_ET.getText().toString().trim()))
                        .build();

                //   String url = "http://wyuzww.nat123.net/UBasketball/login?user_number=" + phoneNumber_ET.getText().toString().trim() + "&user_password=" + passwordNumber_ET.getText().toString().trim();
                HttpClient httpClient = new HttpClient();
                httpClient.request_Post(url, formBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginByPasswordActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        } else if (e instanceof ConnectException) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginByPasswordActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginByPasswordActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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
                            Log.v("Login", responseText);

                            //解析json
                            final User user = JSONObject.parseObject(responseText, User.class);
                            final int code = JSONObject.parseObject(responseText).getInteger("code");
                            final String msg = JSONObject.parseObject(responseText).getString("msg");

                            if (msg.isEmpty() || msg.equals("")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginByPasswordActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                        login_Dialog.dismiss();
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginByPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        login_Dialog.dismiss();
                                        if (code == 0) {
                                            new Utils().saveUser(LoginByPasswordActivity.this, user);

                                            //saveUser(user);
                                            finish();
                                        }
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginByPasswordActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        }
                    }
                });


            } else {
                Toast.makeText(LoginByPasswordActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginByPasswordActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    @Override//获取返回的数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            phoneNumber_ET.setText(data.getStringExtra("phone"));
            passwordNumber_ET.setText(data.getStringExtra("password"));
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }
//    private void getBundle() {
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            phoneNumber_ET.setText(bundle.getString("phone"));
//            passwordNumber_ET.setText(bundle.getString("password"));
//        }
//    }

    private void changeShowPassword() {
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

    private void loadZH() {
        //从本地读取帐号密码
        isRemember = user_ZH_preferences.getBoolean("remember_password", false);
        String phoneNumber = user_ZH_preferences.getString("phoneNumber", "");
        phoneNumber_ET.setText(phoneNumber);
        if (isRemember) {
            String password = user_ZH_preferences.getString("password", "");
            passwordNumber_ET.setText(password);
            remember_password_CB.setChecked(true);
        }
        phoneNumber_ET.setSelection(phoneNumber_ET.getText().toString().trim().length());
        passwordNumber_ET.setSelection(passwordNumber_ET.getText().toString().trim().length());
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
