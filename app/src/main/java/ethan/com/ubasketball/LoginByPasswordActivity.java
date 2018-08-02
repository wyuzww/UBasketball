package ethan.com.ubasketball;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginByPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phoneNumber;
    private EditText passwordNumber;
    private TextView loginByMessage;
    private TextView toRegister;
    private Button okLogin;

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

    }

    private void init() {
        loginByMessage = (TextView) findViewById(R.id.login_bymessage_id);
        toRegister = (TextView) findViewById(R.id.to_register_id);
        okLogin = (Button) findViewById(R.id.login_ok_id);
        loginByMessage.setOnClickListener(this);
        phoneNumber = (EditText) findViewById(R.id.phone_number_id);
        passwordNumber = (EditText) findViewById(R.id.password_number_id);
        toRegister.setOnClickListener(this);
        okLogin.setOnClickListener(this);
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
        }
    }

    private void getBundle() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            phoneNumber.setText(bundle.getString("phone"));
            passwordNumber.setText(bundle.getString("password"));
        }
    }


}
