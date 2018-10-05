package com.ethan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ethan.R;
import com.ethan.activity.MainActivity;
import com.ethan.activity.news.NewsDisplayActvivity;
import com.ethan.adapter.NewsAdapter;
import com.ethan.entity.News;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements GestureDetector.OnGestureListener, SwipeRefreshLayout.OnRefreshListener {
    private MainActivity.MyOnTouchListener onTouchListener;

    private GestureDetector gestureDetector;
    private boolean isShow = true;
    final int TOP = 1, BOTTOM = 2, LEFT = 3, RIGHT = 4;


    private LinearLayout main_title_LL;
    private SwipeRefreshLayout swipeRefresh_SR;

    private List<News> newsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gestureDetector = new GestureDetector(getActivity(), this);

        getNews();
        bindView();

        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);

        ListView listView = (ListView) getActivity().findViewById(R.id.main_listview);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = newsList.get(i);
                Intent intent = new Intent(getActivity(), NewsDisplayActvivity.class);
                intent.putExtra("news_url", news.getNews_url());
                startActivity(intent);
//                Toast.makeText(getActivity(), news.getNews_author(), Toast.LENGTH_SHORT).show();
            }
        });

        onTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                gestureDetector.onTouchEvent(ev);
                return false;
            }
        };
        ((MainActivity) getActivity()).registerMyOnTouchListener(onTouchListener);


    }

    private void bindView() {
        main_title_LL = getActivity().findViewById(R.id.main_line1);
        swipeRefresh_SR = getActivity().findViewById(R.id.swipe_refresh_id);


        swipeRefresh_SR.setOnRefreshListener(this);
    }

    private void getNews() {
        for (int i = 0; i < 5; i++) {
            News news = new News(1, "就在刚刚！男篮国家队主帅之争落下帷幕，杜锋重回广东执教", "2018-09-26 16:46", "背身单打美如画",
                    "http:\\/\\/mini.eastday.com\\/mobile\\/180926164603341.html",
                    "http:\\/\\/01imgmini.eastday.com\\/mobile\\/20180926\\/20180926164603_40f5706f97e861a38bc6666da44d3dfc_2_mwpm_03200403.jpg",
                    "http:\\/\\/01imgmini.eastday.com\\/mobile\\/20180926\\/20180926164603_40f5706f97e861a38bc6666da44d3dfc_3_mwpm_03200403.jpg",
                    "http:\\/\\/01imgmini.eastday.com\\/mobile\\/20180926\\/20180926164603_40f5706f97e861a38bc6666da44d3dfc_1_mwpm_03200403.jpg");
            newsList.add(news);
            news = new News(2, "曾接受湖人试训，双二十数据仅次于姚明，如今他孑然退役默默无闻", "2018-09-27 20:13", "球盲",
                    "http://mini.eastday.com/mobile/180927201328049.html",
                    "http://04imgmini.eastday.com/mobile/20180927/20180927_ef8a95e2e322261f2c827ee0bae3cb52_cover_mwpm_03200403.jpg",
                    "http://04imgmini.eastday.com/mobile/20180927/20180927_9ef516d9e9a370b2172a1324a7a2014f_cover_mwpm_03200403.jpg",
                    "http://04imgmini.eastday.com/mobile/20180927/20180927_638425198f63301ef9658ce6add7f154_cover_mwpm_03200403.jpg");
            newsList.add(news);
            news = new News(3, "又一次拒绝勇士亿元报价，核心悍将离队，科尔：尊重他的决定", "2018-09-28 11:55", "体育讯",
                    "http://mini.eastday.com/mobile/180928115531756.html",
                    "http://09imgmini.eastday.com/mobile/20180928/20180928115531_49ea76a7edbc5f4bef56298a4ca04241_2_mwpm_03200403.jpg",
                    "http://09imgmini.eastday.com/mobile/20180928/20180928115531_49ea76a7edbc5f4bef56298a4ca04241_1_mwpm_03200403.jpg",
                    "http://09imgmini.eastday.com/mobile/20180928/20180928115531_49ea76a7edbc5f4bef56298a4ca04241_3_mwpm_03200403.jpg");
            newsList.add(news);
        }
    }


    public void executeAction(int action) {
        if (isShow) {
            main_title_LL.setVisibility(View.VISIBLE);
        } else {
            main_title_LL.setVisibility(View.GONE);
        }
        switch (action) {
            case 1:
                swipeRefresh_SR.setRefreshing(false);
//                Toast.makeText(getActivity(), "上", Toast.LENGTH_SHORT).show();
                break;
            case 2:
//                Toast.makeText(getActivity(), "下", Toast.LENGTH_SHORT).show();
                break;
            case 3:
//                Toast.makeText(getActivity(), "左", Toast.LENGTH_SHORT).show();
                break;
            case 4:
//                Toast.makeText(getActivity(), "右", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float xExcursion = e1.getX() - e2.getX();
        float yExcursion = e1.getY() - e2.getY();
//        Toast.makeText(getActivity(), String.valueOf(distanceX) + "  "+ String.valueOf(distanceY), Toast.LENGTH_SHORT).show();
        if (distanceY > 10 && Math.abs(yExcursion) > 10 && isShow) {
            isShow = false;
            executeAction(TOP);
        } else if (distanceY < -10 && Math.abs(yExcursion) > 10 && !isShow) {
            isShow = true;
            executeAction(BOTTOM);
        } else {
            return false;
        }
//        if (distanceY>1) {
//            executeAction(TOP);
//        } else if (distanceY<1) {
//            executeAction(BOTTOM);
//        } else if (distanceX<1) {
//            executeAction(LEFT);
//        } else {
//            executeAction(RIGHT);
//        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        float xExcursion = e1.getX() - e2.getX();
//        float yExcursion = e1.getY() - e2.getY();
//        if (yExcursion > 0 && Math.abs(yExcursion) > Math.abs(xExcursion)) {
//            executeAction(TOP);
//        } else if (yExcursion < 0 && Math.abs(yExcursion) > Math.abs(xExcursion)) {
//            executeAction(BOTTOM);
//        } else if (xExcursion > 0 && Math.abs(xExcursion) > Math.abs(yExcursion)) {
//            executeAction(LEFT);
//        } else {
//            executeAction(RIGHT);
//        }
        return false;
    }

    @Override
    public void onRefresh() {

    }
}
