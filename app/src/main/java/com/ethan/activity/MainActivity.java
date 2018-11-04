package com.ethan.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.activity.game.ScoreCounterActivity;
import com.ethan.fragment.FindFragment;
import com.ethan.fragment.MainFragment;
import com.ethan.fragment.MoodFragment;
import com.ethan.fragment.UserFragment;
import com.ethan.util.Utils;
import com.ethan.util.control.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    //底部四个点击TextView
    private TextView txt_main;
    private TextView txt_mood;
    private TextView txt_find;
    private TextView txt_user;
    //ImageView
    private ImageView txt_add;
    //fragmet嵌入在这里
    private FrameLayout main_frameLayout;
    //Fragment管理
    private FragmentManager fragmentManager;
    //Fragment类
    private MainFragment mainFragment;
    private MoodFragment moodFragment;
    private FindFragment findFragment;
    private UserFragment userFragment;
    //管理Fragmenty的图标和字体
    private List<TextView> tv_list;
    //保存的fragment
    private int fragement_index = 0;
    //判断是否要退出
    private static boolean isExit = false;


    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.textSelectColor));
//        }
        new Utils().setFullScreen(getWindow());

        initUI();

        if (savedInstanceState != null && savedInstanceState.getBoolean("isDestroy", false)) {
            fragement_index = savedInstanceState.getInt("Fragement_index", 0);
            onClick(tv_list.get(fragement_index));
        } else {

            init();
        }
        ActivityCollector.addActivity(this);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putInt("Fragement_index", fragement_index);
        outState.putBoolean("isDestroy", true);
    }


    private void initUI() {
        txt_main = (TextView) findViewById(R.id.txt_main);
        txt_mood = (TextView) findViewById(R.id.txt_mood);
        txt_find = (TextView) findViewById(R.id.txt_find);
        txt_user = (TextView) findViewById(R.id.txt_user);
        txt_add = findViewById(R.id.txt_add);
        //framlayout获取
        main_frameLayout = (FrameLayout) findViewById(R.id.main_fragment_container);
        //设置监听器
        txt_main.setOnClickListener(this);
        txt_user.setOnClickListener(this);
        txt_find.setOnClickListener(this);
        txt_mood.setOnClickListener(this);
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBallGame();
            }
        });

        tv_list = new ArrayList<>();
        tv_list.add(txt_main);
        tv_list.add(txt_mood);
        tv_list.add(txt_find);
        tv_list.add(txt_user);


    }

    private void init() {


        //选中第一页
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mainFragment = new MainFragment();
        transaction.add(R.id.main_fragment_container, mainFragment);
        transaction.commit();
        changeFragmentSelect(0);
    }

    @Override
    public void onClick(View view) {
        txt_add.setSelected(false);
        //v4包导入getSupportFragmentManager，app包使用getFragmentManager，app包3.0后才使用
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()) {

            case R.id.txt_main:
                changeFragmentSelect(0);
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.main_fragment_container, mainFragment);
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case R.id.txt_mood:
                changeFragmentSelect(1);
                if (moodFragment == null) {
                    moodFragment = new MoodFragment();
                    transaction.add(R.id.main_fragment_container, moodFragment);
                } else {
                    transaction.show(moodFragment);
                }
                break;
//            case R.id.txt_add:
//                addBallGame();
//                break;
            case R.id.txt_find:
                changeFragmentSelect(2);
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.main_fragment_container, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case R.id.txt_user:
                changeFragmentSelect(3);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.main_fragment_container, userFragment);
                } else {
                    transaction.show(userFragment);
                }
                break;

        }
        transaction.commit();

    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (moodFragment != null) {
            transaction.hide(moodFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }



    //管理Fragmenty的图标和字体
    private void changeFragmentSelect(int index) {
        for (int i = 0; i < tv_list.size(); i++) {
            if (index == i) {
                fragement_index = i;
                tv_list.get(i).setTextColor(getResources().getColor(R.color.textSelectColor));
                tv_list.get(i).setSelected(true);
            } else {
                tv_list.get(i).setTextColor(getResources().getColor(R.color.textColor));
                tv_list.get(i).setSelected(false);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //利用Handler来更改isExit
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (JCVideoPlayer.backPress()) {
                return true;
            }
            exit();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*
    点击两次退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ActivityCollector.finishAll();
            finish();
            System.exit(0);
        }
    }

    private void addBallGame() {
        final String item[] = {"默认赛制", "自定义赛制"};
        for (int i = 0; i < tv_list.size(); i++) {
            tv_list.get(i).setTextColor(getResources().getColor(R.color.textColor));
            tv_list.get(i).setSelected(false);
        }
        if (txt_add.isSelected()) {
            txt_add.setSelected(false);
            changeFragmentSelect(fragement_index);
        } else {
            txt_add.setSelected(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("创建比赛").create();
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    addBallGame();
                }
            });
            builder.setItems(item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            startActivity(new Intent(MainActivity.this, ScoreCounterActivity.class));
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this, "暂不支持", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    dialog.dismiss();
                    addBallGame();
                }
            });
            builder.show();
        }

    }

    //fragment触摸事件分发，将触摸事件分发给每个能够响应的fragment
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }
}
