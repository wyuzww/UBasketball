package com.ethan.activity.userfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.adapter.UserAdapter;
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

public class FollowActivity extends AppCompatActivity implements ListItemClickHelp, View.OnClickListener {
    private Button back_BT;
    private ListView follow_listview_LV;
    private UserAdapter userAdapter;

    private ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        new Utils().setFullScreen(getWindow());

        bindView();

        getFollowData(new Utils().getUser(this));

        setAdapter();

    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        follow_listview_LV = findViewById(R.id.follow_listview_id);

        back_BT.setOnClickListener(this);

    }

    private void creatMoods(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = new User(users.get(i).getUser_id(), null, new HttpClient().getURL() + users.get(i).getUser_image(), users.get(i).getUser_name(), null, users.get(i).getUser_sex(), users.get(i).getUser_birth(), users.get(i).getUser_signature(), null);
            userArrayList.add(user);
        }
    }

    private void getFollowData(User user) {
        String url = "GetUser";
        FormBody formBody = new FormBody.Builder()
                .add("following_user_id", String.valueOf(user.getUser_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("users");

                    final List<User> users = JSONObject.parseArray(string, User.class);
//                    Log.v("###",users.toString());


                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FollowActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                userArrayList.clear();
                                creatMoods(users);
                                userAdapter.notifyDataSetChanged();

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FollowActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }


    private void setAdapter() {
        userAdapter = new UserAdapter(this, R.layout.users_item, this, userArrayList);
        follow_listview_LV.setAdapter(userAdapter);
        follow_listview_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toUserHomePage(userArrayList.get(position));
            }
        });

    }

    private void toUserHomePage(User user) {
        Intent intent = new Intent(this, UserHomePageActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

        Toast.makeText(FollowActivity.this, user.getUser_name(), Toast.LENGTH_SHORT).show();
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
            case R.id.to_follow_id:
                deleteFollow(new Utils().getUser(this).getUser_id(), userArrayList.get(position).getUser_id());
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
                                Toast.makeText(FollowActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FollowActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(FollowActivity.this, msg, Toast.LENGTH_SHORT).show();
                                getFollowData(new Utils().getUser(FollowActivity.this));

                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FollowActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FollowActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
