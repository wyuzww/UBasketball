package com.ethan.activity.userfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.adapter.CommentAdapter;
import com.ethan.entity.Comment;
import com.ethan.entity.Comment_User;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.network.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;
    private ListView message_listview_LV;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        new Utils().setFullScreen(getWindow());

        bindView();

        getComments(new Utils().getUser(this));

        setCommentAdapter();

    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        message_listview_LV = findViewById(R.id.message_listview_id);

        back_BT.setOnClickListener(this);

    }

    private void getComments(User user) {
        //查询与我相关的评论
        String url = "GetComment";
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
                            Toast.makeText(MessageActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MessageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MessageActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final String responseText = response.body().string();


                    String string = JSONObject.parseObject(responseText).getString("comments");

                    final List<Comment_User> comment_users = JSONObject.parseArray(string, Comment_User.class);
//                    Log.v("###",comment_users.toString());


                    final int code = JSONObject.parseObject(responseText).getInteger("code");
                    final String msg = JSONObject.parseObject(responseText).getString("msg");

                    if (msg.isEmpty() || msg.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (code == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(MoodContentActivity.this, msg, Toast.LENGTH_SHORT).show();
                                commentArrayList.clear();
                                creatComments(comment_users);
                                commentAdapter.notifyDataSetChanged();

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MessageActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void creatComments(List<Comment_User> comment_users) {
        for (int i = 0; i < comment_users.size(); i++) {
            User user = new User(comment_users.get(i).getUser_id(), null, new HttpClient().getURL() + comment_users.get(i).getUser_image(), comment_users.get(i).getUser_name(), null, comment_users.get(i).getUser_sex(), comment_users.get(i).getUser_birth(), comment_users.get(i).getUser_signature(), null);
            Comment comment = new Comment(comment_users.get(i).getComment_id(), comment_users.get(i).getMood_id(), user, comment_users.get(i).getComment_text(), comment_users.get(i).getComment_time());
            commentArrayList.add(comment);
        }
    }

    private void setCommentAdapter() {
        commentAdapter = new CommentAdapter(this, R.layout.comments_item, commentArrayList);
        message_listview_LV.setAdapter(commentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

}
