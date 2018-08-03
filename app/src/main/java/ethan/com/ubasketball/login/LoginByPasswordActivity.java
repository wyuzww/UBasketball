package ethan.com.ubasketball.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ethan.com.ubasketball.MainActivity;
import ethan.com.ubasketball.R;
import ethan.com.ubasketball.util.ActivityCollector;

public class LoginByPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber_ET;
    private EditText passwordNumber_ET;
    private TextView loginByMessage;
    private TextView toRegister;
    private Button okLogin;
    private Button showPassword_BT;
    private Button back_BT;
    private boolean showPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bypassword);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();
        getBundle();

        ActivityCollector.addActivity(this);

    }

    private void init() {
        loginByMessage = (TextView) findViewById(R.id.login_bymessage_id);
        toRegister = (TextView) findViewById(R.id.to_register_id);
        okLogin = (Button) findViewById(R.id.login_ok_id);
        loginByMessage.setOnClickListener(this);
        phoneNumber_ET = (EditText) findViewById(R.id.phone_number_id);
        passwordNumber_ET = (EditText) findViewById(R.id.password_number_id);
        showPassword_BT = (Button) findViewById(R.id.login_show_password_id);
        back_BT = (Button) findViewById(R.id.back_button);
        toRegister.setOnClickListener(this);
        okLogin.setOnClickListener(this);
        showPassword_BT.setOnClickListener(this);
        back_BT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bymessage_id:
                startActivity(new Intent(LoginByPasswordActivity.this, LoginByMessageActivity.class));
                break;
            case R.id.to_register_id:
                startActivity(new Intent(LoginByPasswordActivity.this, RegisterActivity.class));
                break;
            case R.id.login_ok_id:
                //startActivity(new Intent());
                break;
            case R.id.login_show_password_id:
                ChangeShowPassword();
                break;
            case R.id.back_button:
                startActivity(new Intent(LoginByPasswordActivity.this, MainActivity.class));
                break;
        }
    }

    private void getBundle() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            phoneNumber_ET.setText(bundle.getString("phone"));
            passwordNumber_ET.setText(bundle.getString("password"));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
