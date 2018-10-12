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

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private LinearLayout update_LL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        new Utils().setFullScreen(getWindow());

        bindView();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        update_LL = findViewById(R.id.update_id);

        back_BT.setOnClickListener(this);
        update_LL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_id:
                checkUpdate();
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    private void checkUpdate() {
        new AlertDialog.Builder(AboutActivity.this)
                .setMessage("       这是最新版本！")
                .create().show();
    }
}
