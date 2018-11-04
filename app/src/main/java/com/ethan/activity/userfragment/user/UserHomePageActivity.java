package com.ethan.activity.userfragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.ethan.util.view.MyListView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class UserHomePageActivity extends AppCompatActivity implements ListItemClickHelp, View.OnClickListener {
    private Button back_BT;

    private ImageView user_image_IV;
    private TextView user_name_TV;
    private ImageView user_sex_IV;
    private TextView user_signature_TV;
    private ImageView to_follow_IV;
    private MyListView myListView;
    private TextView following_TV;
    private TextView followed_TV;
    private TextView mood_amount_TV;
    private LinearLayout homepage_title_LL;
    private LinearLayout homepage_panel_LL;
    private TextView look_more_TV;


    private MoodAdapter moodAdapter;
    private int homepageColor;
    private User user;


    private ArrayList<Mood> moodArrayList = new ArrayList<>();
    private ArrayList<Integer> clockArrayList = new ArrayList<>();
    private ArrayList<Integer> loveArrayList = new ArrayList<>();
    private ArrayList<Integer> followingArrayList = new ArrayList<>();
    //    private ArrayList<Integer> followedArrayList = new ArrayList<>();
    private ArrayList<String> strings;

    private boolean isFollowing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_homepage);
        bindView();
        getUserData();
        new Utils().setFullScreenCustomColor(getWindow(), homepageColor);
        getMoodData();
        setMoodAdapter();

    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        user_image_IV = findViewById(R.id.user_image_id);
        user_name_TV = findViewById(R.id.user_name_id);
        user_sex_IV = findViewById(R.id.user_sex_id);
        user_signature_TV = findViewById(R.id.user_signature_id);
        to_follow_IV = findViewById(R.id.to_follow_id);
        myListView = findViewById(R.id.homepage_mood_listview_id);
        following_TV = findViewById(R.id.following_id);
        followed_TV = findViewById(R.id.followed_id);
        mood_amount_TV = findViewById(R.id.mood_amount_id);
        homepage_title_LL = findViewById(R.id.homepage_title_id);
        homepage_panel_LL = findViewById(R.id.homepage_panel_id);
        look_more_TV = findViewById(R.id.look_more_id);


        look_more_TV.setOnClickListener(this);
        following_TV.setOnClickListener(this);
        followed_TV.setOnClickListener(this);
        to_follow_IV.setOnClickListener(this);
        back_BT.setOnClickListener(this);
        user_image_IV.setOnClickListener(this);
    }

    private void getUserData() {
        user = (User) getIntent().getSerializableExtra("user");
        initHomePage(user);
    }

    private void getMoodData() {
        String url = "GetMood";
        FormBody formBody = new FormBody.Builder()
                .add("user_id", String.valueOf(user.getUser_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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


                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                moodArrayList.clear();
                                creatMoods(mood_users);
                                mood_amount_TV.setText(String.valueOf(moodArrayList.size()));
                                moodAdapter.notifyDataSetChanged();

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                clockArrayList.clear();
                                clockArrayList.addAll(integers);
//                                moodAdapter.notifyDataSetChanged();

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
//                                moodAdapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            }
        });
    }

    private void getFollowing(User user) {
        String url = "GetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("following_user_id", String.valueOf(user.getUser_id()))
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
//                                followingArrayList.addAll(integers);
                                following_TV.setText(String.valueOf(integers.size()));
                            }
                        });
                    }
                }
            }
        });
    }

    private void getFollowed(User user) {
        String url = "GetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("followed_user_id", String.valueOf(user.getUser_id()))
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
//                                followedArrayList.addAll(integers);
                                followed_TV.setText(String.valueOf(integers.size()));
                            }
                        });
                    }
                }
            }
        });
    }

    private void getFollowingByUser(User user, final User home_user) {
        String url = "GetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("following_user_id", String.valueOf(user.getUser_id()))
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
                                followingArrayList.clear();
                                followingArrayList.addAll(integers);
                                if (followingArrayList.contains(home_user.getUser_id())) {
                                    isFollowing = true;
                                    to_follow_IV.setImageResource(R.drawable.ic_cfollow);
                                } else {
                                    isFollowing = false;
                                    to_follow_IV.setImageResource(R.drawable.ic_follow);
                                }
//                                following_TV.setText(String.valueOf(integers.size()));
                            }
                        });
                    }
                }
            }
        });
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

    private void initHomePage(User user) {
        //初始化头像
        Picasso.with(this).load(user.getUser_image())
                .error(R.drawable.ic_user_icon)
                .placeholder(R.drawable.ic_user_icon)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(user_image_IV);
        //初始化昵称、性别
        user_name_TV.setText(user.getUser_name());
        if (user.getUser_sex().equals("男")) {
            user_sex_IV.setVisibility(View.VISIBLE);
            user_sex_IV.setImageResource(R.drawable.ic_man);
            homepageColor = R.color.manhomepage;
            homepage_title_LL.setBackgroundResource(R.color.manhomepage);
            homepage_panel_LL.setBackgroundResource(R.color.manhomepage);
        } else if (user.getUser_sex().equals("女")) {
            user_sex_IV.setVisibility(View.VISIBLE);
            user_sex_IV.setImageResource(R.drawable.ic_woman);
            homepageColor = R.color.womanhomepage;
            homepage_title_LL.setBackgroundResource(R.color.womanhomepage);
            homepage_panel_LL.setBackgroundResource(R.color.womanhomepage);
        } else {
            user_sex_IV.setVisibility(View.GONE);
            homepageColor = R.color.manhomepage;
            homepage_title_LL.setBackgroundResource(R.color.manhomepage);
            homepage_panel_LL.setBackgroundResource(R.color.manhomepage);
        }
        user_signature_TV.setText(user.getUser_signature());

        getFollowing(user);
        getFollowed(user);
        initUser();


    }

    private void initUser() {
        if (new Utils().isLogined(this)) {
            if (new Utils().getUser(this).getUser_id() == user.getUser_id()) {
                to_follow_IV.setVisibility(View.GONE);
            } else {
                to_follow_IV.setVisibility(View.VISIBLE);
                getFollowingByUser(new Utils().getUser(this), user);
//                Toast.makeText(UserHomePageActivity.this,new Utils().getUser(this).getUser_id()+"",Toast.LENGTH_SHORT).show();


            }
            getClock(new Utils().getUser(this));
            getLove(new Utils().getUser(this));

        } else {
            to_follow_IV.setImageResource(R.drawable.ic_follow);
        }
    }

    private void setMoodAdapter() {
        moodAdapter = new MoodAdapter(this, R.layout.moods_item, this, moodArrayList, clockArrayList, loveArrayList);
        myListView.setAdapter(moodAdapter);
        myListView.setFocusable(false);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toMoodContent(moodArrayList.get(i), clockArrayList, loveArrayList);
                Toast.makeText(UserHomePageActivity.this, moodArrayList.get(i).getUser().getUser_name(), Toast.LENGTH_SHORT).show();
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
            case R.id.user_image_id:
                new Utils().toShowImage(this, user.getUser_image());
                break;
            case R.id.to_follow_id:
                if (new Utils().isLogined(this)) {
                    changeFollow(new Utils().getUser(this).getUser_id(), user.getUser_id());
                } else {
                    Toast.makeText(UserHomePageActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.look_more_id:
                Toast.makeText(UserHomePageActivity.this, "暂不可用！", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void changeFollow(int following_user_id, int followed_user_id) {
        if (isFollowing) {
            deleteFollow(following_user_id, followed_user_id);
        } else {
            setFollow(following_user_id, followed_user_id);
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

    public void setFollow(int following_user_id, int followed_user_id) {
        String url = "SetInteger";
        FormBody formBody = new FormBody.Builder()
                .add("following_user_id", String.valueOf(following_user_id))
                .add("followed_user_id", String.valueOf(followed_user_id))
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

                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");
                    if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, msg, Toast.LENGTH_SHORT).show();
                                isFollowing = true;
                                to_follow_IV.setImageResource(R.drawable.ic_cfollow);
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void deleteFollow(int following_user_id, int followed_user_id) {
        String url = "DeleteInteger";
        FormBody formBody = new FormBody.Builder()
                .add("following_user_id", String.valueOf(following_user_id))
                .add("followed_user_id", String.valueOf(followed_user_id))
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

                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");
                    if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, msg, Toast.LENGTH_SHORT).show();
                                isFollowing = false;
                                to_follow_IV.setImageResource(R.drawable.ic_follow);
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserHomePageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
