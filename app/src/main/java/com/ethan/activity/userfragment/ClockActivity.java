package com.ethan.activity.userfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.moodfragment.MoodContentActivity;
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

public class ClockActivity extends AppCompatActivity implements ListItemClickHelp, View.OnClickListener {
    private Button back_BT;
    private ListView clock_listview_LV;

    private MoodAdapter moodAdapter;
    private ArrayList<Mood> moodArrayList = new ArrayList<>();
    private ArrayList<Integer> clockArrayList = new ArrayList<>();
    private ArrayList<Integer> loveArrayList = new ArrayList<>();
    private ArrayList<String> strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Utils().setFullScreen(getWindow());
        setContentView(R.layout.activity_clock);
//        new Utils().setFullScreen(getWindow());

        bindView();

        getMoodData(new Utils().getUser(this));

        setAdapter();

    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        clock_listview_LV = findViewById(R.id.clock_listview_id);

        back_BT.setOnClickListener(this);

    }

    private void getMoodData(User user) {
        String url = "GetMood";
        FormBody formBody = new FormBody.Builder()
                .add("clock_user_id", String.valueOf(user.getUser_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClockActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ClockActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClockActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClockActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                moodArrayList.clear();
                                creatMoods(mood_users);
                                moodAdapter.notifyDataSetChanged();

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClockActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ClockActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void creatMoods(List<Mood_User> mood_users) {

        if (new Utils().isLogined(this)) {
            getClock(new Utils().getUser(this));
            getLove(new Utils().getUser(this));
        } else {
            clockArrayList.clear();
            loveArrayList.clear();
        }
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
//            clockArrayListi.add(mood.getMood_id());
            moodArrayList.add(mood);
        }
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
                        runOnUiThread(new Runnable() {
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
                        runOnUiThread(new Runnable() {
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
        moodAdapter = new MoodAdapter(this, R.layout.moods_item, this, moodArrayList, clockArrayList, loveArrayList);

        clock_listview_LV.setAdapter(moodAdapter);
        clock_listview_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toMoodContent(moodArrayList.get(i), clockArrayList, loveArrayList);
                Toast.makeText(ClockActivity.this, moodArrayList.get(i).getUser().getUser_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toMoodContent(Mood mood, ArrayList<Integer> clockArrayListi, ArrayList<Integer> loveArrayList) {
        Intent intent = new Intent(this, MoodContentActivity.class);
        intent.putExtra("mood", mood);
        intent.putIntegerArrayListExtra("clockArrayListi", clockArrayListi);
        intent.putIntegerArrayListExtra("loveArrayList", loveArrayList);

        startActivity(intent);
//        startActivity(new Intent(getActivity(), MoodContentActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void onClick(int position, View v) {
        switch (v.getId()) {
            case R.id.mood_clock_id:
                if (new Utils().isLogined(this)) {
                    if (!v.isSelected()) {
                        Utils.setClock(this, new Utils().getUser(this).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, clockArrayList, moodAdapter);
//                        setClock(new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position);
                    } else {
                        Utils.deleteClock(this, new Utils().getUser(this).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, clockArrayList, moodAdapter);
//                        deleteClock(new Utils().getUser(getActivity()).getUser_id(), moodArrayList.get(position).getMood_id(), position);
                    }
                    moodAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mood_love_id:
                if (new Utils().isLogined(this)) {
                    if (!v.isSelected()) {
                        Utils.setLove(this, new Utils().getUser(this).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, loveArrayList, moodAdapter);
                    } else {
                        Utils.deleteLove(this, new Utils().getUser(this).getUser_id(), moodArrayList.get(position).getMood_id(), position, moodArrayList, loveArrayList, moodAdapter);
                    }
                    moodAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
