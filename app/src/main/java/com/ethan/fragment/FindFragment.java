package com.ethan.fragment;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.MainActivity;
import com.ethan.activity.findfragment.FindActivity;
import com.ethan.activity.userfragment.HelpActivity;
import com.ethan.adapter.VideoAdapter;
import com.ethan.entity.Video;
import com.ethan.util.network.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.SENSOR_SERVICE;

public class FindFragment extends Fragment implements GestureDetector.OnGestureListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private LinearLayout find_layout_LL;
    private ImageView add_menu_IV;
    private TextView to_find_TV;
    private ListView listView;

    private SwipeRefreshLayout find_swipeRefresh_SR;
    private MainActivity.MyOnTouchListener onTouchListener;
    private GestureDetector gestureDetector;
    final int TOP = 1, BOTTOM = 2, LEFT = 3, RIGHT = 4;


    private VideoAdapter videoAdapter;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;


    private ArrayList<Video> videoArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        bindView();

        initVideo();

        setAdapter();

        gestureDetector = new GestureDetector(getActivity(), this);
        onTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                gestureDetector.onTouchEvent(ev);
                return false;
            }
        };
        ((MainActivity) getActivity()).registerMyOnTouchListener(onTouchListener);
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

    }


    private void setAdapter() {
        videoAdapter = new VideoAdapter(getActivity(), R.layout.videoview_item, videoArrayList);
        listView.setAdapter(videoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Video video = videoArrayList.get(position);
//                videoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void bindView() {
        find_layout_LL = getActivity().findViewById(R.id.find_layout_id);
        add_menu_IV = getActivity().findViewById(R.id.add_menu_id);
        listView = (ListView) getActivity().findViewById(R.id.find_listview);
        find_swipeRefresh_SR = getActivity().findViewById(R.id.find_swipe_refresh_id);
        to_find_TV = getActivity().findViewById(R.id.to_find_id);


        to_find_TV.setOnClickListener(this);
        find_layout_LL.setOnClickListener(this);
        add_menu_IV.setOnClickListener(this);
        find_swipeRefresh_SR.setOnRefreshListener(this);
        find_swipeRefresh_SR.post(new Runnable() {
            @Override
            public void run() {
                find_swipeRefresh_SR.setRefreshing(true);
                initVideo();
            }
        });
        find_swipeRefresh_SR.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.textSelectColor));
        find_swipeRefresh_SR.setColorSchemeResources(R.color.white);
    }

    private void initVideo() {
        String url = "GetVideo";
        HttpClient httpClient = new HttpClient();
        httpClient.request_Get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
                            find_swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                } else if (e instanceof ConnectException) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            find_swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "网络出错", Toast.LENGTH_SHORT).show();
                            find_swipeRefresh_SR.setRefreshing(false);
                        }
                    });
                }
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("videos");

                    final List<Video> videos = JSONObject.parseArray(string, Video.class);
//                    Log.v("###",videos.toString());


                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                find_swipeRefresh_SR.setRefreshing(false);
                            }
                        });
                    } else if (code == 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                videoArrayList.clear();
                                for (int i = 0; i < videos.size(); i++) {
                                    videoArrayList.add(videos.get(i));
                                }
                                videoAdapter.notifyDataSetChanged();
                                find_swipeRefresh_SR.setRefreshing(false);

                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                find_swipeRefresh_SR.setRefreshing(false);
                            }
                        });
                    }

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "连接到服务器错误", Toast.LENGTH_SHORT).show();
                            find_swipeRefresh_SR.setRefreshing(false);
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
                find_swipeRefresh_SR.setRefreshing(false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_layout_id:
                startActivity(new Intent(getActivity(), FindActivity.class));
                break;
            case R.id.add_menu_id:
                showAddMenu(v);
                break;
            case R.id.to_find_id:
                startActivity(new Intent(getActivity(), FindActivity.class));
                break;
        }
    }

    private void showAddMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.add_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_user_id:
                        startActivity(new Intent(getActivity(), FindActivity.class));
                        break;
                    case R.id.scan_id:
                        Toast.makeText(getActivity(), "扫一扫暂不可用", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help_id:
                        startActivity(new Intent(getActivity(), HelpActivity.class));
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.e("###", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
        Log.e("###", "onPause");
    }

    @Override
    public void onRefresh() {
        initVideo();
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
        if (distanceY > 10 && Math.abs(yExcursion) > 10) {
            executeAction(TOP);
        } else if (distanceY < -10 && Math.abs(yExcursion) > 10) {
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
}

