package com.ethan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.MainActivity;
import com.ethan.activity.findfragment.FindActivity;
import com.ethan.activity.moodfragment.MoodContentActivity;
import com.ethan.activity.moodfragment.WriteMoodActivity;
import com.ethan.activity.userfragment.MessageActivity;
import com.ethan.adapter.MoodAdapter;
import com.ethan.entity.Mood;
import com.ethan.entity.Mood_User;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.network.HttpClient;
import com.ethan.util.view.ListItemClickHelp;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MoodFragment extends Fragment implements ListItemClickHelp, GestureDetector.OnGestureListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //红点
    private CheckBox mood_red_point_CB;
    private TextView title_mood_all_TV;
    private TextView title_mood_mine_TV;
    private ImageView mood_search_IV;
    private ImageView mood_mine_msg_IV;
    private FloatingActionButton float_add_mood_BT;
    private SwipeRefreshLayout swipeRefresh_SR;
    private ListView listView;

    private MainActivity.MyOnTouchListener onTouchListener;
    private GestureDetector gestureDetector;
    final int TOP = 1, BOTTOM = 2, LEFT = 3, RIGHT = 4;

    private MoodAdapter moodAdapter;
    private ArrayList<Mood> moodArrayList = new ArrayList<>();
    private ArrayList<Integer> clockArrayList = new ArrayList<>();
    private ArrayList<Integer> loveArrayList = new ArrayList<>();
    private ArrayList<String> strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gestureDetector = new GestureDetector(getActivity(), this);

        bindView();

        setAdapter();

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
        mood_red_point_CB = getActivity().findViewById(R.id.mood_red_point_id);
        title_mood_all_TV = getActivity().findViewById(R.id.title_mood_all_id);
        title_mood_mine_TV = getActivity().findViewById(R.id.title_mood_myfollow_id);
        float_add_mood_BT = getActivity().findViewById(R.id.float_add_mood_button);
        mood_search_IV = getActivity().findViewById(R.id.mood_search_id);
        mood_mine_msg_IV = getActivity().findViewById(R.id.mood_mine_msg_id);
        swipeRefresh_SR = getActivity().findViewById(R.id.mood_swipe_refresh_id);
        listView = (ListView) getActivity().findViewById(R.id.mood_listview);

        title_mood_all_TV.setOnClickListener(this);
        title_mood_mine_TV.setOnClickListener(this);
        float_add_mood_BT.setOnClickListener(this);
        mood_search_IV.setOnClickListener(this);
        mood_mine_msg_IV.setOnClickListener(this);
        swipeRefresh_SR.setOnRefreshListener(this);
        swipeRefresh_SR.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh_SR.setRefreshing(true);
                if (new Utils().isLogined(getActivity())) {
                    getClock(new Utils().getUser(getActivity()));
                    getLove(new Utils().getUser(getActivity()));
                } else {
                    clockArrayList.clear();
                    loveArrayList.clear();
                }
                initMoods();
            }
        });
        swipeRefresh_SR.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.textSelectColor));
        swipeRefresh_SR.setColorSchemeResources(R.color.white);
    }

    private void creatMoods(List<Mood_User> mood_users) {

//        strings.add(new HttpClient().getURL()+ ( (2%2== 0) ? "userImage/13612250853.jpg" : "userImage/13612250852.jpg"));
        for (int i = 0; i < mood_users.size(); i++) {
            String[] url = mood_users.get(i).getMood_images_url().split(",");
            strings = new ArrayList<>();
            User user = new User(mood_users.get(i).getUser_id(), null, new HttpClient().getURL() + mood_users.get(i).getUser_image(), mood_users.get(i).getUser_name(), null, mood_users.get(i).getUser_sex(), mood_users.get(i).getUser_birth(), mood_users.get(i).getUser_signature(), null);

            for (int j = 0; j < mood_users.get(i).getMood_images_amount(); j++) {
//                Toast.makeText(getActivity(), url[j], Toast.LENGTH_SHORT).show();

                strings.add(new HttpClient().getURL() + url[j].trim());
//                Log.d("##########",strings.get(j));

            }

            Mood mood = new Mood(mood_users.get(i).getMood_id(), user, mood_users.get(i).getMood_text(), mood_users.get(i).getMood_time(), mood_users.get(i).getMood_images_amount(), mood_users.get(i).getMood_clocks_amount(), mood_users.get(i).getMood_comments_amount(), mood_users.get(i).getMood_loves_amount(), strings);
            moodArrayList.add(mood);
        }
    }

    private void initMoods() {
        String url = "GetMood";
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


                    String string = JSONObject.parseObject(responseText).getString("moods");

                    final List<Mood_User> mood_users = JSONObject.parseArray(string, Mood_User.class);
                    Log.v("###", mood_users.toString());


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
                                moodArrayList.clear();
                                creatMoods(mood_users);
                                moodAdapter.notifyDataSetChanged();
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

    private void getClock(User user) {
        String url = "GetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("clock_user_id", String.valueOf(user.getUser_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("integers");

                    final List<Integer> integers = JSONObject.parseArray(string, Integer.class);
                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");
                    if (code == 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                clockArrayList.clear();
                                clockArrayList.addAll(integers);
                                moodAdapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            }
        });
    }

    private void getLove(User user) {
        String url = "GetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("love_user_id", String.valueOf(user.getUser_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("integers");

                    final List<Integer> integers = JSONObject.parseArray(string, Integer.class);
                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");
                    if (code == 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                loveArrayList.clear();
                                loveArrayList.addAll(integers);
                                moodAdapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            }
        });
    }

    private void setAdapter() {
        moodAdapter = new MoodAdapter(getActivity(), R.layout.moods_item, this, moodArrayList, clockArrayList, loveArrayList);


        listView.setAdapter(moodAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                toMoodContent(moodArrayList.get(i), clockArrayList, loveArrayList);
//                Toast.makeText(getActivity(), moodArrayList.get(i).getUser().getUser_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_mood_myfollow_id:
                if (new Utils().isLogined(getActivity())) {
                    title_mood_mine_TV.setTextSize(20);
                    title_mood_all_TV.setTextSize(15);
                    Toast.makeText(getActivity(), "我的关注", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.title_mood_all_id:
                title_mood_mine_TV.setTextSize(15);
                title_mood_all_TV.setTextSize(20);
                Toast.makeText(getActivity(), "所有动态", Toast.LENGTH_SHORT).show();
                break;
            case R.id.float_add_mood_button:
                if (new Utils().isLogined(getActivity())) {
                    Toast.makeText(getActivity(), "添加动态", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), WriteMoodActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.mood_search_id:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), FindActivity.class));
                break;
            case R.id.mood_mine_msg_id:
                if (new Utils().isLogined(getActivity())) {
//                    Toast.makeText(getActivity(), "msg", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void toMoodContent(Mood mood, ArrayList<Integer> clockArrayListi, ArrayList<Integer> loveArrayList) {
        Intent intent = new Intent(getActivity(), MoodContentActivity.class);
        intent.putExtra("mood", mood);
        intent.putIntegerArrayListExtra("clockArrayListi", clockArrayListi);
        intent.putIntegerArrayListExtra("loveArrayList", loveArrayList);

        startActivity(intent);
//        startActivity(new Intent(getActivity(), MoodContentActivity.class));
    }

    @Override
    public void onRefresh() {
        if (new Utils().isLogined(getActivity())) {
            getClock(new Utils().getUser(getActivity()));
            getLove(new Utils().getUser(getActivity()));
        } else {
            clockArrayList.clear();
            loveArrayList.clear();
        }
        initMoods();
        mood_red_point_CB.setVisibility(View.GONE);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    public void onResume() {
        super.onResume();
        if (new Utils().isLogined(getActivity())) {
            getClock(new Utils().getUser(getActivity()));
            getLove(new Utils().getUser(getActivity()));
        } else {
            clockArrayList.clear();
            loveArrayList.clear();

        }
        moodAdapter.notifyDataSetChanged();
        initMoods();
//        Toast.makeText(getActivity(), "Re", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(int position, View v) {
        switch (v.getId()) {
            case R.id.mood_clock_id:
                if (new Utils().isLogined(getActivity())) {
                    if (!v.isSelected()) {
                        Utils.setClock(getActivity(), new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, clockArrayList, moodAdapter);
//                        setClock(new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position);
                    } else {
                        Utils.deleteClock(getActivity(), new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, clockArrayList, moodAdapter);
//                        deleteClock(new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position);
                    }
                    moodAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mood_love_id:
                if (new Utils().isLogined(getActivity())) {
                    if (!v.isSelected()) {
                        Utils.setLove(getActivity(), new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, loveArrayList, moodAdapter);
                    } else {
                        Utils.deleteLove(getActivity(), new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, loveArrayList, moodAdapter);
                    }
                    moodAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
