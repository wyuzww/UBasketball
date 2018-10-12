package com.ethan.activity.userfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ethan.R;
import com.ethan.util.Utils;

public class SecurityActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private LinearLayout security_reset_password_LL;
    private LinearLayout security_reset_phone_LL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        new Utils().setFullScreen(getWindow());

        bindView();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        security_reset_password_LL = findViewById(R.id.security_reset_password_id);
        security_reset_phone_LL = findViewById(R.id.security_reset_phone_id);

        back_BT.setOnClickListener(this);
        security_reset_password_LL.setOnClickListener(this);
        security_reset_phone_LL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_reset_password_id:
                setting("修改密码");
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.security_reset_phone_id:
                setting("更换手机号");
                break;
        }
    }

    private void setting(String msg) {
        new AlertDialog.Builder(SecurityActivity.this)
                .setMessage("      " + msg)
                .create().show();
    }
}
