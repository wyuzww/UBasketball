package com.ethan.activity.userfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ethan.R;
import com.ethan.util.Utils;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Utils().setFullScreen(getWindow());
        setContentView(R.layout.activity_clock);
        new Utils().setFullScreen(getWindow());

        bindView();
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);

        back_BT.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

}
