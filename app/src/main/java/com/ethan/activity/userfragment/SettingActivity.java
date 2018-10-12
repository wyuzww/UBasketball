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

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private LinearLayout setting_new_msg_LL;
    private LinearLayout setting_secret_LL;
    private LinearLayout setting_remove_cache_LL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        new Utils().setFullScreen(getWindow());

        bindView();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        setting_new_msg_LL = findViewById(R.id.setting_new_msg_id);
        setting_secret_LL = findViewById(R.id.setting_secret_id);
        setting_remove_cache_LL = findViewById(R.id.setting_remove_cache_id);

        back_BT.setOnClickListener(this);
        setting_new_msg_LL.setOnClickListener(this);
        setting_secret_LL.setOnClickListener(this);
        setting_remove_cache_LL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_remove_cache_id:
                setting("清除缓存");
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.setting_new_msg_id:
                setting("消息与通知");
                break;
            case R.id.setting_secret_id:
                setting("隐私");
                break;
        }
    }

    private void setting(String msg) {
        new AlertDialog.Builder(SettingActivity.this)
                .setMessage("      " + msg)
                .create().show();
    }
}
