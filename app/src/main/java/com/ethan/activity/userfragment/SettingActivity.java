package com.ethan.activity.userfragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ethan.R;
import com.ethan.util.Utils;
import com.ethan.util.manager.FileCacheUtils;

import java.io.File;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private LinearLayout setting_new_msg_LL;
    private LinearLayout setting_secret_LL;
    private LinearLayout setting_remove_cache_LL;
    private TextView cacheSize_TV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        new Utils().setFullScreen(getWindow());

        bindView();

        initCache();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        setting_new_msg_LL = findViewById(R.id.setting_new_msg_id);
        setting_secret_LL = findViewById(R.id.setting_secret_id);
        setting_remove_cache_LL = findViewById(R.id.setting_remove_cache_id);
        cacheSize_TV = findViewById(R.id.cache_size_id);

        back_BT.setOnClickListener(this);
        setting_new_msg_LL.setOnClickListener(this);
        setting_secret_LL.setOnClickListener(this);
        setting_remove_cache_LL.setOnClickListener(this);
    }

    private void initCache() {

        //获得应用内部缓存(/data/data/com.***.***/cache)
        File inCache = new File(this.getCacheDir().getPath());
        //获得应用外部缓存/mnt/sdcard/android/data/com.xxx.xxx/cache
        File outCache = new File(this.getExternalCacheDir().getPath());


        try {
            String cacheSize = FileCacheUtils.getFormatSize(FileCacheUtils.getFolderSize(inCache) + FileCacheUtils.getFolderSize(outCache));
            cacheSize_TV.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_remove_cache_id:
                cleanCache();
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

    private void cleanCache() {
        ProgressDialog clean_dialog = new ProgressDialog(this);
        clean_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        clean_dialog.setCancelable(false);
        clean_dialog.setCanceledOnTouchOutside(false);
        clean_dialog.setMessage("正在清理...");
        clean_dialog.show();

        FileCacheUtils.cleanExternalCache(this);
        FileCacheUtils.cleanInternalCache(this);


        initCache();

        clean_dialog.dismiss();

    }
}
