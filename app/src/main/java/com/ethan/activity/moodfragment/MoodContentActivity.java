package com.ethan.activity.moodfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.userfragment.login.LoginByPasswordActivity;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.adapter.CommentAdapter;
import com.ethan.adapter.MoodImageShowAdapter;
import com.ethan.entity.Comment;
import com.ethan.entity.Comment_User;
import com.ethan.entity.Mood;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.network.HttpClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MoodContentActivity extends AppCompatActivity implements View.OnClickListener {
    private Mood mood;
    private java.util.ArrayList<Integer> clockArrayListi = new ArrayList<>();
    private ArrayList<Integer> loveArrayList = new ArrayList<>();
    private ArrayList<Comment> commentArrayList = new ArrayList<>();

    private CommentAdapter commentAdapter;

    private ImageView mood_user_image_IV;
    private TextView mood_user_name_TV;
    private ImageView mood_user_sex_IV;
    private TextView mood_time_TV;
    private TextView mood_text_TV;
    private GridView mood_image_GV;
    private TextView mood_clock_TV;
    private TextView mood_comment_TV;
    private TextView mood_love_TV;
    private TextView mood_comment_amount_TV;
    private ListView comment_listview_LV;
    private Button back_BT;
    private ImageView get_expression_IV;
    private EditText comment_text_ET;
    private TextView send_comment_TV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_content);


        bindView();

        getMood();

        getComments(mood);

        setCommentAdapter();


    }

    private void bindView() {
        mood_user_image_IV = (ImageView) findViewById(R.id.mood_user_image_id);
        mood_user_name_TV = (TextView) findViewById(R.id.mood_user_name_id);
        mood_user_sex_IV = (ImageView) findViewById(R.id.mood_user_sex_id);
        mood_time_TV = (TextView) findViewById(R.id.mood_time_id);
        mood_text_TV = (TextView) findViewById(R.id.mood_text_id);
        mood_image_GV = (GridView) findViewById(R.id.mood_image_gv_id);
        mood_clock_TV = (TextView) findViewById(R.id.mood_clock_id);
        mood_comment_TV = (TextView) findViewById(R.id.mood_comment_id);
        mood_love_TV = (TextView) findViewById(R.id.mood_love_id);
        mood_comment_amount_TV = (TextView) findViewById(R.id.mood_comment_amount_id);
        comment_listview_LV = findViewById(R.id.mood_comment_listview_id);
        get_expression_IV = findViewById(R.id.get_expression_id);
        comment_text_ET = findViewById(R.id.comment_text_id);
        send_comment_TV = findViewById(R.id.send_comment_id);
        back_BT = findViewById(R.id.back_button);


        get_expression_IV.setOnClickListener(this);
        send_comment_TV.setOnClickListener(this);

        back_BT.setOnClickListener(this);
        mood_user_image_IV.setOnClickListener(this);

    }

    private void getMood() {
        mood = (Mood) getIntent().getSerializableExtra("mood");
        clockArrayListi = getIntent().getIntegerArrayListExtra("clockArrayListi");
        loveArrayList = getIntent().getIntegerArrayListExtra("loveArrayList");

        Picasso.with(MoodContentActivity.this).load(mood.getUser().getUser_image())
                .error(R.drawable.ic_user_icon)
                .placeholder(R.drawable.ic_user_icon)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(mood_user_image_IV);
        mood_user_name_TV.setText(mood.getUser().getUser_name());

        if (mood.getUser().getUser_sex().equals("男")) {
            mood_user_sex_IV.setVisibility(View.VISIBLE);
            mood_user_sex_IV.setImageResource(R.drawable.ic_man);
        } else if (mood.getUser().getUser_sex().equals("女")) {
            mood_user_sex_IV.setVisibility(View.VISIBLE);
            mood_user_sex_IV.setImageResource(R.drawable.ic_woman);
        } else {
            mood_user_sex_IV.setVisibility(View.GONE);
        }

        mood_time_TV.setText(mood.getMood_time());
        mood_text_TV.setText(mood.getMood_text());
        //初始化照片gridview
        mood_image_GV.setAdapter(new MoodImageShowAdapter(MoodContentActivity.this, R.layout.mood_image_show_gv_items, mood.getMood_images_url()));
        //初始化收藏、评论、点赞
        if (mood.getMood_clocks_amount() > 0) {
            mood_clock_TV.setText(String.valueOf(mood.getMood_clocks_amount()));
        } else {
            mood_clock_TV.setText("收藏");
        }
        if (mood.getMood_comments_amount() > 0) {
            mood_comment_TV.setText(String.valueOf(mood.getMood_comments_amount()));
        } else {
            mood_comment_TV.setText("评论");
        }
        if (mood.getMood_loves_amount() > 0) {
            mood_love_TV.setText(String.valueOf(mood.getMood_loves_amount()));
        } else {
            mood_love_TV.setText("点赞");
        }
        if (!clockArrayListi.contains(mood.getMood_id())) {
            mood_clock_TV.setSelected(false);
        } else {
            mood_clock_TV.setSelected(true);
        }
        if (!loveArrayList.contains(mood.getMood_id())) {
            mood_love_TV.setSelected(false);
        } else {
            mood_love_TV.setSelected(true);
        }

        mood_comment_amount_TV.setText(String.valueOf(mood.getMood_comments_amount()));

    }

//    private void getComments1() {
//        //查询数据库评论
//        commentArrayList = new ArrayList<>();
//        for (int i=0;i<mood.getMood_comments_amount();i++) {
//            User user = new User(i, String.valueOf(i), i % 2 == 0 ? new HttpClient().getURL() + "userImage/13612250853.jpg" : new HttpClient().getURL() + "userImage/13612250852.jpg", "people " + String.valueOf(i), "123456", i % 2 == 0 ? "男" : "女", "1996-11-16", "Write the code , change the world!", "123456");
//            Comment comment = new Comment(i,mood.getMood_id(),user,"评论内容 "+i, "1996-11-16");
//            commentArrayList.add(comment);
//        }
//    }

    private void getComments(Mood mood) {
        String url = "GetComment";
        FormBody formBody = new FormBody.Builder()
                .add("mood_id", String.valueOf(mood.getMood_id()))
                .build();
        HttpClient httpClient = new HttpClient();
        httpClient.request_Post(url, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoodContentActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (e instanceof ConnectException) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MoodContentActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoodContentActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MoodContentActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MoodContentActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoodContentActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void setCommentAdapter() {

        commentAdapter = new CommentAdapter(this, R.layout.comments_item, commentArrayList);
        comment_listview_LV.setAdapter(commentAdapter);
    }

    private void creatComments(List<Comment_User> comment_users) {

        for (int i = 0; i < comment_users.size(); i++) {
            User user = new User(comment_users.get(i).getUser_id(), null, new HttpClient().getURL() + comment_users.get(i).getUser_image(), comment_users.get(i).getUser_name(), null, comment_users.get(i).getUser_sex(), comment_users.get(i).getUser_birth(), comment_users.get(i).getUser_signature(), null);
            Comment comment = new Comment(comment_users.get(i).getComment_id(), comment_users.get(i).getMood_id(), user, comment_users.get(i).getComment_text(), comment_users.get(i).getComment_time());
            commentArrayList.add(comment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.mood_user_image_id:
                Intent intent = new Intent(this, UserHomePageActivity.class);
                intent.putExtra("user", mood.getUser());
                this.startActivity(intent);
                break;
            case R.id.send_comment_id:
                if (new Utils().isLogined(this)) {
                    if (!comment_text_ET.getText().toString().trim().equals("")) {
                        //强制收起键盘
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MoodContentActivity.this.getCurrentFocus().getWindowToken(), 0);
                        sendComment();
                    } else {
                        Toast.makeText(MoodContentActivity.this, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MoodContentActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.get_expression_id:
                Toast.makeText(MoodContentActivity.this, "暂不支持！", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void sendComment() {
        String url = "WriteComment";
        FormBody formBody = new FormBody.Builder()
                .add("user_token", new Utils().getUser(this).getUser_token())
                .add("mood_id", String.valueOf(mood.getMood_id()))
                .add("comment_text", comment_text_ET.getText().toString())
                .add("comment_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
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
                                Toast.makeText(MoodContentActivity.this, msg, Toast.LENGTH_SHORT).show();
                                getComments(mood);
                            }
                        });


                    } else if (code == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MoodContentActivity.this)
                                        .setMessage(msg + ",请重新登录")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(MoodContentActivity.this, LoginByPasswordActivity.class));
                                                finish();
                                            }
                                        }).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MoodContentActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoodContentActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
