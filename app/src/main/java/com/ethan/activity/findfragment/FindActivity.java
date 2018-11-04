package com.ethan.activity.findfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;

public class FindActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView to_find_TV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find);


    }

    private void bindView() {
        to_find_TV = findViewById(R.id.to_find_id);

        to_find_TV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_find_id:
                Toast.makeText(FindActivity.this, "暂不可用！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
