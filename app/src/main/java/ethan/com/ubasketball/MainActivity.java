package ethan.com.ubasketball;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ethan.com.ubasketball.fragment.ClockFragment;
import ethan.com.ubasketball.fragment.MainFragment;
import ethan.com.ubasketball.fragment.SearchFragment;
import ethan.com.ubasketball.fragment.UserFragment;
import ethan.com.ubasketball.util.ActivityCollector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //底部四个点击TextView
    private TextView txt_main;
    private TextView txt_clock;
    private TextView txt_search;
    private TextView txt_user;
    //fragmet嵌入在这里
    private FrameLayout main_frameLayout;
    //Fragment管理
    private FragmentManager fragmentManager;
    //Fragment类
    private MainFragment mainFragment;
    private ClockFragment clockFragment;
    private SearchFragment searchFragment;
    private UserFragment userFragment;
    //管理Fragmenty的图标和字体
    private List<TextView> tv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initUI();
        init();

        ActivityCollector.addActivity(this);
    }


    private void initUI() {
        txt_main = (TextView) findViewById(R.id.txt_main);
        txt_clock = (TextView) findViewById(R.id.txt_clock);
        txt_search = (TextView) findViewById(R.id.txt_search);
        txt_user = (TextView) findViewById(R.id.txt_user);
        //framlayout获取
        main_frameLayout = (FrameLayout) findViewById(R.id.main_fragment_container);
        //设置监听器
        txt_main.setOnClickListener(this);
        txt_user.setOnClickListener(this);
        txt_search.setOnClickListener(this);
        txt_clock.setOnClickListener(this);

    }

    private void init() {
        tv_list = new ArrayList<>();
        tv_list.add(txt_main);
        tv_list.add(txt_clock);
        tv_list.add(txt_search);
        tv_list.add(txt_user);

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
            case R.id.txt_clock:
                changeFragmentSelect(1);
                if (clockFragment == null) {
                    clockFragment = new ClockFragment();
                    transaction.add(R.id.main_fragment_container, clockFragment);
                } else {
                    transaction.show(clockFragment);
                }
                break;
            case R.id.txt_search:
                changeFragmentSelect(2);
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(R.id.main_fragment_container, searchFragment);
                } else {
                    transaction.show(searchFragment);
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
    public void hideAllFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (clockFragment != null) {
            transaction.hide(clockFragment);
        }
        if (searchFragment != null) {
            transaction.hide(searchFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    //管理Fragmenty的图标和字体
    private void changeFragmentSelect(int index) {
        for (int i = 0; i < tv_list.size(); i++) {
            if (index == i) {
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
}
