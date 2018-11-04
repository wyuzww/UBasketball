package com.ethan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.MainActivity;
import com.ethan.activity.mainfragment.NewsDisplayActvivity;
import com.ethan.adapter.NewsAdapter;
import com.ethan.entity.News;
import com.ethan.util.network.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainFragment extends Fragment implements GestureDetector.OnGestureListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private MainActivity.MyOnTouchListener onTouchListener;
    private GestureDetector gestureDetector;
    private NewsAdapter newsAdapter;
    private boolean isShow = true;
    private boolean isNews = true;
    final int TOP = 1, BOTTOM = 2, LEFT = 3, RIGHT = 4;


    private LinearLayout main_title_LL;
    private LinearLayout main_news_LL;
    private LinearLayout main_game_LL;
    private LinearLayout game_LL;
    private TextView title_news_TV;
    private TextView title_game_TV;
    private ListView listView;
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


        bindView();


        setNewsAdapter();

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
        main_news_LL = getActivity().findViewById(R.id.main_line2);
        main_game_LL = getActivity().findViewById(R.id.main_line3);
        game_LL = getActivity().findViewById(R.id.game_LL_id);
        title_news_TV = getActivity().findViewById(R.id.title_news_id);
        title_game_TV = getActivity().findViewById(R.id.title_game_id);
        swipeRefresh_SR = getActivity().findViewById(R.id.swipe_refresh_id);


        game_LL.setOnClickListener(this);
        title_news_TV.setOnClickListener(this);
        title_game_TV.setOnClickListener(this);
        swipeRefresh_SR.setOnRefreshListener(this);
        swipeRefresh_SR.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh_SR.setRefreshing(true);
                getNews();
            }
        });
        swipeRefresh_SR.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.textSelectColor));
        swipeRefresh_SR.setColorSchemeResources(R.color.white);

    }

    private void setNewsAdapter() {
        newsAdapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);

        listView = (ListView) getActivity().findViewById(R.id.main_listview);
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
    }

    private void getNews() {
        String url = "GetNews";
        HttpClient httpClient = new HttpClient();
        httpClient.request_Get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
                            swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                } else if (e instanceof ConnectException) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "网络出错", Toast.LENGTH_SHORT).show();
                            swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                }
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("news");

                    final List<News> news = JSONObject.parseArray(string, News.class);
                    Log.v("###", news.toString());


                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                swipeRefresh_SR.setRefreshing(false);
                            }
                        });
                    } else if (code == 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                newsList.clear();
                                for (int i = 0; i < news.size(); i++) {
                                    newsList.add(news.get(i));
                                }
                                newsAdapter.notifyDataSetChanged();
                                swipeRefresh_SR.setRefreshing(false);

                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                swipeRefresh_SR.setRefreshing(false);
                            }
                        });
                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "连接到服务器错误", Toast.LENGTH_SHORT).show();
                            swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }




    public void executeAction(int action) {
//        if (isShow) {
//            main_title_LL.setVisibility(View.VISIBLE);
//        } else {
//            main_title_LL.setVisibility(View.GONE);
//        }
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
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onRefresh() {
        getNews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_news_id:
                if (!isNews) {
                    isNews = true;
                    title_news_TV.setTextSize(20);
                    title_game_TV.setTextSize(15);
                    main_news_LL.setVisibility(View.VISIBLE);
                    main_game_LL.setVisibility(View.GONE);
                }
                break;
            case R.id.title_game_id:
                if (isNews) {
                    isNews = false;
                    title_news_TV.setTextSize(15);
                    title_game_TV.setTextSize(20);
                    main_news_LL.setVisibility(View.GONE);
                    main_game_LL.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.game_LL_id:
                Toast.makeText(getActivity(), "暂不可用！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
