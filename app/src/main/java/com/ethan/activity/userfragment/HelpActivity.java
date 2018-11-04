package com.ethan.activity.userfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.util.Utils;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private EditText bug_title_ET;
    private EditText bug_content_ET;
    private Button commit_ok_BT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        new Utils().setFullScreen(getWindow());

        bindView();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        bug_title_ET = findViewById(R.id.bug_title_id);
        bug_content_ET = findViewById(R.id.bug_content_id);
        commit_ok_BT = findViewById(R.id.commit_ok_id);

        back_BT.setOnClickListener(this);
        commit_ok_BT.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.commit_ok_id:
                Toast.makeText(this, "暂不可用！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
