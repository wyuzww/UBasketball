package com.ethan.activity.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.entity.Team;

public class ScoreCounterActivity extends AppCompatActivity implements View.OnClickListener {
    //Main
    private Team leftTeam = new Team();
    private Team rightTeam = new Team();
    private boolean isPause = true;
    private boolean isLeftPause = false;
    private boolean isRightPause = false;
    private boolean isLeftChange = false;
    private boolean isRightChange = false;
    private boolean isBeginGame = false;
    private boolean leftFlag = false;
    private boolean rightFlag = false;
    private int left_player_view[] = {-1, -1, -1, -1, -1};//场上球员
    private int right_player_view[] = {-1, -1, -1, -1, -1};


    //球员颜色控制
    final private int Before_OnClick_Player_Background = R.drawable.rectangle_frame_gray_shape;
    final private int OnClick_Player_Background = R.color.Red;

    //点击事件
    //左边球员
    private LinearLayout left_team_top_player_LL;
    private LinearLayout left_team_middle_left_player_LL;
    private LinearLayout left_team_middle_right_player_LL;
    private LinearLayout left_team_bottom_left_player_LL;
    private LinearLayout left_team_bottom_right_player_LL;
    //右边球员
    private LinearLayout right_team_top_player_LL;
    private LinearLayout right_team_middle_left_player_LL;
    private LinearLayout right_team_middle_right_player_LL;
    private LinearLayout right_team_bottom_left_player_LL;
    private LinearLayout right_team_bottom_right_player_LL;
    //Button
    private Button game_over_BT;
    private Button game_statistics_BT;
    private Button left_team_request_pause_BT;
    private Button right_team_request_pause_BT;
    private Button left_team_request_change_BT;
    private Button right_team_request_change_BT;
    //TextView
    private TextView twentyfour_seconds_TV;
    private TextView left_ball_team_name_TV;
    private TextView right_ball_team_name_TV;
    //ImageView
    private ImageView all_pause_IV;

    //非点击控件
    //顶部标题处
    private TextView left_ball_team_TV;
    private TextView right_ball_team_TV;
    private TextView game_quarter_TV;
    private TextView minutes_TV;
    private TextView seconds_TV;
    private TextView left_team_score_TV;
    private TextView right_team_score_TV;
    //球员号码、姓名
    //左边球员
    private TextView left_team_top_player_number_TV;
    private TextView left_team_top_player_name_TV;
    private TextView left_team_middle_left_player_number_TV;
    private TextView left_team_middle_left_player_name_TV;
    private TextView left_team_middle_right_player_number_TV;
    private TextView left_team_middle_right_player_name_TV;
    private TextView left_team_bottom_left_player_number_TV;
    private TextView left_team_bottom_left_player_name_TV;
    private TextView left_team_bottom_right_player_number_TV;
    private TextView left_team_bottom_right_player_name_TV;
    //右边球员
    private TextView right_team_top_player_number_TV;
    private TextView right_team_top_player_name_TV;
    private TextView right_team_middle_left_player_number_TV;
    private TextView right_team_middle_left_player_name_TV;
    private TextView right_team_middle_right_player_number_TV;
    private TextView right_team_middle_right_player_name_TV;
    private TextView right_team_bottom_left_player_number_TV;
    private TextView right_team_bottom_left_player_name_TV;
    private TextView right_team_bottom_right_player_number_TV;
    private TextView right_team_bottom_right_player_name_TV;
    //底部TextView
    private TextView left_team_foul_TV;
    private TextView left_team_pause_TV;
    private TextView right_team_foul_TV;
    private TextView right_team_pause_TV;
    //else
    private LinearLayout score_bottom_linearLayout_LL;
    private FrameLayout pull_frameLayout_FL;
    private ImageView pull_img_IV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_score_plate);

        bindView();

        initGameSetting();

    }


    private void bindView() {
        //点击事件
        //左边球员
        left_team_top_player_LL = findViewById(R.id.left_team_top_player_id);
        left_team_middle_left_player_LL = findViewById(R.id.left_team_middle_left_player_id);
        left_team_middle_right_player_LL = findViewById(R.id.left_team_middle_right_player_id);
        left_team_bottom_left_player_LL = findViewById(R.id.left_team_bottom_left_player_id);
        left_team_bottom_right_player_LL = findViewById(R.id.left_team_bottom_right_player_id);
        //右边球员
        right_team_top_player_LL = findViewById(R.id.right_team_top_player_id);
        right_team_middle_left_player_LL = findViewById(R.id.right_team_middle_left_player_id);
        right_team_middle_right_player_LL = findViewById(R.id.right_team_middle_right_player_id);
        right_team_bottom_left_player_LL = findViewById(R.id.right_team_bottom_left_player_id);
        right_team_bottom_right_player_LL = findViewById(R.id.right_team_bottom_right_player_id);
        //Button
        game_over_BT = findViewById(R.id.game_over_id);
        game_statistics_BT = findViewById(R.id.game_statistics_id);
        left_team_request_pause_BT = findViewById(R.id.left_team_request_pause_id);
        right_team_request_pause_BT = findViewById(R.id.right_team_request_pause_id);
        left_team_request_change_BT = findViewById(R.id.left_team_request_change_id);
        right_team_request_change_BT = findViewById(R.id.right_team_request_change_id);
        //TextView
        twentyfour_seconds_TV = findViewById(R.id.twentyfour_seconds_id);
        left_ball_team_name_TV = findViewById(R.id.left_ball_team_name_id);
        right_ball_team_name_TV = findViewById(R.id.right_ball_team_name_id);
        //ImageView
        all_pause_IV = findViewById(R.id.all_pause_id);

        //非点击控件
        //顶部标题处
        left_ball_team_TV = findViewById(R.id.left_ball_team_id);
        right_ball_team_TV = findViewById(R.id.right_ball_team_id);
        game_quarter_TV = findViewById(R.id.game_quarter_id);
        minutes_TV = findViewById(R.id.minutes_id);
        seconds_TV = findViewById(R.id.seconds_id);
        left_team_score_TV = findViewById(R.id.left_team_score_id);
        right_team_score_TV = findViewById(R.id.right_team_score_id);
        //球员号码、姓名
        //左边球员
        left_team_top_player_number_TV = findViewById(R.id.left_team_top_player_number_id);
        left_team_top_player_name_TV = findViewById(R.id.left_team_top_player_name_id);
        left_team_middle_left_player_number_TV = findViewById(R.id.left_team_middle_left_player_number_id);
        left_team_middle_left_player_name_TV = findViewById(R.id.left_team_middle_left_player_name_id);
        left_team_middle_right_player_number_TV = findViewById(R.id.left_team_middle_right_player_number_id);
        left_team_middle_right_player_name_TV = findViewById(R.id.left_team_middle_right_player_name_id);
        left_team_bottom_left_player_number_TV = findViewById(R.id.left_team_bottom_left_player_number_id);
        left_team_bottom_left_player_name_TV = findViewById(R.id.left_team_bottom_left_player_name_id);
        left_team_bottom_right_player_number_TV = findViewById(R.id.left_team_bottom_right_player_number_id);
        left_team_bottom_right_player_name_TV = findViewById(R.id.left_team_bottom_right_player_name_id);
        //右边球员
        right_team_top_player_number_TV = findViewById(R.id.right_team_top_player_number_id);
        right_team_top_player_name_TV = findViewById(R.id.right_team_top_player_name_id);
        right_team_middle_left_player_number_TV = findViewById(R.id.right_team_middle_left_player_number_id);
        right_team_middle_left_player_name_TV = findViewById(R.id.right_team_middle_left_player_name_id);
        right_team_middle_right_player_number_TV = findViewById(R.id.right_team_middle_right_player_number_id);
        right_team_middle_right_player_name_TV = findViewById(R.id.right_team_middle_right_player_name_id);
        right_team_bottom_left_player_number_TV = findViewById(R.id.right_team_bottom_left_player_number_id);
        right_team_bottom_left_player_name_TV = findViewById(R.id.right_team_bottom_left_player_name_id);
        right_team_bottom_right_player_number_TV = findViewById(R.id.right_team_bottom_right_player_number_id);
        right_team_bottom_right_player_name_TV = findViewById(R.id.right_team_bottom_right_player_name_id);
        //底部TextView
        left_team_foul_TV = findViewById(R.id.left_team_foul_id);
        left_team_pause_TV = findViewById(R.id.left_team_pause_id);
        right_team_foul_TV = findViewById(R.id.right_team_foul_id);
        right_team_pause_TV = findViewById(R.id.right_team_pause_id);
        //else
        score_bottom_linearLayout_LL = findViewById(R.id.score_plate_bottom_id);
        pull_frameLayout_FL = findViewById(R.id.pull_up_down_id);
        pull_img_IV = findViewById(R.id.pull_img_id);

        //左边球员
        left_team_top_player_LL.setOnClickListener(this);
        left_team_middle_left_player_LL.setOnClickListener(this);
        left_team_middle_right_player_LL.setOnClickListener(this);
        left_team_bottom_left_player_LL.setOnClickListener(this);
        left_team_bottom_right_player_LL.setOnClickListener(this);
        //右边球员
        right_team_top_player_LL.setOnClickListener(this);
        right_team_middle_left_player_LL.setOnClickListener(this);
        right_team_middle_right_player_LL.setOnClickListener(this);
        right_team_bottom_left_player_LL.setOnClickListener(this);
        right_team_bottom_right_player_LL.setOnClickListener(this);
        //Button
        game_over_BT.setOnClickListener(this);
        game_statistics_BT.setOnClickListener(this);
        left_team_request_pause_BT.setOnClickListener(this);
        right_team_request_pause_BT.setOnClickListener(this);
        left_team_request_change_BT.setOnClickListener(this);
        right_team_request_change_BT.setOnClickListener(this);
        //TextView
        twentyfour_seconds_TV.setOnClickListener(this);
        left_ball_team_name_TV.setOnClickListener(this);
        right_ball_team_name_TV.setOnClickListener(this);
        //ImageView
        all_pause_IV.setOnClickListener(this);
        //else
        pull_frameLayout_FL.setOnClickListener(this);


    }


    private void initGameSetting() {
        leftTeam.initPause(5);
        rightTeam.initPause(5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //球员操作
            case R.id.left_team_top_player_id:
                showPlayerEventMenu(v, leftTeam, 1, 0);
//                reView();
                break;
            case R.id.left_team_middle_left_player_id:
                showPlayerEventMenu(v, leftTeam, 1, 1);
//                reView();
                break;
            case R.id.left_team_middle_right_player_id:
                showPlayerEventMenu(v, leftTeam, 1, 2);
//                reView();
                break;
            case R.id.left_team_bottom_left_player_id:
                showPlayerEventMenu(v, leftTeam, 1, 3);
//                reView();
                break;
            case R.id.left_team_bottom_right_player_id:
                showPlayerEventMenu(v, leftTeam, 1, 4);
//                reView();
                break;
            case R.id.right_team_top_player_id:
                showPlayerEventMenu(v, rightTeam, 2, 0);
//                reView();
                break;
            case R.id.right_team_middle_left_player_id:
                showPlayerEventMenu(v, rightTeam, 2, 1);
//                reView();
                break;
            case R.id.right_team_middle_right_player_id:
                showPlayerEventMenu(v, rightTeam, 2, 2);
//                reView();
                break;
            case R.id.right_team_bottom_left_player_id:
                showPlayerEventMenu(v, rightTeam, 2, 3);
//                reView();
                break;
            case R.id.right_team_bottom_right_player_id:
                showPlayerEventMenu(v, rightTeam, 2, 4);
//                reView();
                break;

            //Button
            case R.id.game_over_id:
                Toast.makeText(ScoreCounterActivity.this, "结束比赛", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.game_statistics_id:
                showGameStatistics();
                break;
            case R.id.left_team_request_pause_id:
                if (isBeginGame && !isPause) {
                    isLeftPause = true;
                    leftTeam.setPause(1);
                    {
                        all_pause_IV.setImageResource(R.drawable.ic_pause);
                        isPause = true;
                    }
                    reView();

                    Toast.makeText(ScoreCounterActivity.this, "左边球队请求暂停", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "比赛暂停或未开始", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.right_team_request_pause_id:
                if (isBeginGame && !isPause) {
                    isRightPause = true;
                    rightTeam.setPause(1);
                    {
                        all_pause_IV.setImageResource(R.drawable.ic_pause);
                        isPause = true;
                    }
                    reView();
                    Toast.makeText(ScoreCounterActivity.this, "右边球队请求暂停", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "比赛暂停或未开始", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.left_team_request_change_id:
                if (isBeginGame && !isPause) {
                    isLeftChange = true;
                    {
                        all_pause_IV.setImageResource(R.drawable.ic_pause);
                        isPause = true;
                    }
                    Toast.makeText(ScoreCounterActivity.this, "左边球队请求换人", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "比赛暂停或未开始", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.right_team_request_change_id:
                if (isBeginGame && !isPause) {
                    isRightChange = true;
                    {
                        all_pause_IV.setImageResource(R.drawable.ic_pause);
                        isPause = true;
                    }
                    Toast.makeText(ScoreCounterActivity.this, "右边球队请求换人", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "比赛暂停或未开始", Toast.LENGTH_SHORT).show();
                }
                break;
            //TextView
            case R.id.twentyfour_seconds_id:
                Toast.makeText(ScoreCounterActivity.this, "刷新24秒", Toast.LENGTH_SHORT).show();
                break;
            case R.id.left_ball_team_name_id:
                addTeam(leftTeam, "left");
                Toast.makeText(ScoreCounterActivity.this, "添加主场球队", Toast.LENGTH_SHORT).show();
                break;
            case R.id.right_ball_team_name_id:
                addTeam(rightTeam, "right");
                Toast.makeText(ScoreCounterActivity.this, "添加客场球队", Toast.LENGTH_SHORT).show();
                break;
            //ImageView
            case R.id.all_pause_id:
                if (leftFlag && rightFlag && !isBeginGame) {
                    isBeginGame = true;
                    all_pause_IV.setImageResource(R.drawable.ic_playing);
                    isPause = false;
                    Toast.makeText(ScoreCounterActivity.this, "开始比赛", Toast.LENGTH_SHORT).show();
                } else if (isBeginGame && isPause) {
                    all_pause_IV.setImageResource(R.drawable.ic_playing);
                    isPause = false;
                    Toast.makeText(ScoreCounterActivity.this, "继续比赛", Toast.LENGTH_SHORT).show();
                } else if (isBeginGame && !isPause) {
                    all_pause_IV.setImageResource(R.drawable.ic_pause);
                    Toast.makeText(ScoreCounterActivity.this, "官方暂停", Toast.LENGTH_SHORT).show();
                    isPause = true;
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "请先添加球队", Toast.LENGTH_SHORT).show();
                }
                break;
            //else
            case R.id.pull_up_down_id:
                pullUpOrDown();
                break;
        }
    }

    private void showPlayerEventMenu(final View view, final Team team, int operation, final int position) {
        if (isBeginGame) {
            int index = 0;
            view.setBackgroundColor(getResources().getColor(OnClick_Player_Background));
            PopupMenu menu = new PopupMenu(this, view);


            if (operation == 1 && (isLeftChange || isPause || isLeftPause || isRightPause)) {
                menu.getMenu().add("取消");
                for (index = 0; index < team.getPerson(); index++) {
                    boolean isOnCourt = false;
                    for (int j = 0; j < 5; j++) {
                        if (left_player_view[j] == index) {
                            isOnCourt = true;
                        }
                    }
                    if (!isOnCourt) {
                        menu.getMenu().add(0, index, 0, team.getPlayer(index).getNumber() + "   " + team.getPlayer(index).getName());
                    }
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        for (int i = 0; i < team.getPerson(); i++) {
                            if (item.getItemId() == i) {
                                left_player_view[position] = i;
                                if (position == 0) {
                                    left_team_top_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    left_team_top_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 1) {
                                    left_team_middle_left_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    left_team_middle_left_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 2) {
                                    left_team_middle_right_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    left_team_middle_right_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 3) {
                                    left_team_bottom_left_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    left_team_bottom_left_player_name_TV.setText(team.getPlayer(i).getName());
                                } else {
                                    left_team_bottom_right_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    left_team_bottom_right_player_name_TV.setText(team.getPlayer(i).getName());
                                }
                            }
                        }
                        view.setBackground(getResources().getDrawable(Before_OnClick_Player_Background));
                        return true;
                    }
                });
            } else if (operation == 2 && (isRightChange || isPause || isRightPause || isLeftPause)) {
                menu.getMenu().add("取消");
                for (index = 0; index < team.getPerson(); index++) {
                    boolean isOnCourt = false;
                    for (int j = 0; j < 5; j++) {
                        if (right_player_view[j] == index) {
                            isOnCourt = true;
                        }
                    }
                    if (!isOnCourt) {
                        menu.getMenu().add(0, index, 0, team.getPlayer(index).getNumber() + "   " + team.getPlayer(index).getName());
                    }
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        for (int i = 0; i < team.getPerson(); i++) {
                            if (item.getItemId() == i) {
                                right_player_view[position] = i;
                                if (position == 0) {
                                    right_team_top_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    right_team_top_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 1) {
                                    right_team_middle_left_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    right_team_middle_left_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 2) {
                                    right_team_middle_right_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    right_team_middle_right_player_name_TV.setText(team.getPlayer(i).getName());
                                } else if (position == 3) {
                                    right_team_bottom_left_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    right_team_bottom_left_player_name_TV.setText(team.getPlayer(i).getName());
                                } else {
                                    right_team_bottom_right_player_number_TV.setText(team.getPlayer(i).getNumber());
                                    right_team_bottom_right_player_name_TV.setText(team.getPlayer(i).getName());
                                }
                            }
                        }
                        view.setBackground(getResources().getDrawable(Before_OnClick_Player_Background));
                        return true;
                    }
                });
            } else {
                int number = -1;
                if (operation == 1) {
                    number = left_player_view[position];
                } else {
                    number = right_player_view[position];
                }
                final int finalNumber = number;

                menu.getMenuInflater().inflate(R.menu.player_event_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cancel_id:
                            case R.id.cancel1_id:
                                break;
//                            case R.id.score_id:
//                                view.setBackgroundColor(getResources().getColor(OnClick_Player_Background));
//                                break;
                            case R.id.three_id:
                                team.getPlayer(finalNumber).setThree_point(1);
                                Toast.makeText(ScoreCounterActivity.this, "+3分", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.two_id:
                                team.getPlayer(finalNumber).setTwo_point(1);
                                Toast.makeText(ScoreCounterActivity.this, "+2分", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.one_id:
                                team.getPlayer(finalNumber).setOne_point(1);
                                Toast.makeText(ScoreCounterActivity.this, "+1分", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.assist_id:
                                team.getPlayer(finalNumber).setAssist(1);
                                Toast.makeText(ScoreCounterActivity.this, "助攻+1", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.backboard_id:
                                team.getPlayer(finalNumber).setBackboard(1);
                                Toast.makeText(ScoreCounterActivity.this, "篮板+1", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.block_id:
                                team.getPlayer(finalNumber).setBlock(1);
                                Toast.makeText(ScoreCounterActivity.this, "封盖+1", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.steal_id:
                                team.getPlayer(finalNumber).setSteal(1);
                                Toast.makeText(ScoreCounterActivity.this, "抢断+1", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.foul_id:
                                team.getPlayer(finalNumber).setFoul(1);
                                Toast.makeText(ScoreCounterActivity.this, "犯规+1", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        reView();
                        view.setBackground(getResources().getDrawable(Before_OnClick_Player_Background));
                        return true;
                    }
                });
            }
            menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    view.setBackground(getResources().getDrawable(Before_OnClick_Player_Background));
                }
            });
            menu.show();
            isLeftPause = false;
            isRightPause = false;
            isLeftChange = false;
            isRightChange = false;
        } else {
            Toast.makeText(ScoreCounterActivity.this, "请先开始比赛", Toast.LENGTH_SHORT).show();
        }
    }


    private void pullUpOrDown() {
        if (score_bottom_linearLayout_LL.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pull_frameLayout_FL.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);//去掉ALIGN_PARENT_BOTTOM 规则
            pull_frameLayout_FL.setLayoutParams(params);
            pull_img_IV.setImageResource(R.drawable.ic_pulldown);
            score_bottom_linearLayout_LL.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pull_frameLayout_FL.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//添加ALIGN_PARENT_BOTTOM 规则
            pull_frameLayout_FL.setLayoutParams(params);
            pull_img_IV.setImageResource(R.drawable.ic_pullup);
            score_bottom_linearLayout_LL.setVisibility(View.GONE);
        }
    }

    private void addTeam(final Team team, final String direction) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(ScoreCounterActivity.this);
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        final View view = LayoutInflater.from(ScoreCounterActivity.this).inflate(R.layout.add_team_dialog, null);
        builder.setView(view);


        final AlertDialog dialog = builder.show();

        dialog.setCanceledOnTouchOutside(false);

        //final int finalPerson = person;
        view.findViewById(R.id.add_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int person = 0;
                int number_id = 0;
                int name_id = 0;
                String FieldName1;
                String FieldName2;
                EditText editText;
                EditText editText1;
                EditText editText2;
                editText = view.findViewById(R.id.add_team_name_id);
                for (int i = 1; i <= 10; i++) {
                    FieldName1 = "player" + String.valueOf(i) + "_number_id";
                    FieldName2 = "player" + String.valueOf(i) + "_name_id";
                    try {
                        number_id = R.id.class.getField(FieldName1).getInt(null);
                        name_id = R.id.class.getField(FieldName2).getInt(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    editText1 = view.findViewById(number_id);
                    editText2 = view.findViewById(name_id);
                    if (!editText1.getText().toString().trim().equals("") && !editText2.getText().toString().trim().equals("")) {
//                            team.setPlayer(i);
                        team.getPlayer(i - 1).setNumber(editText1.getText().toString().trim());
                        team.getPlayer(i - 1).setName(editText2.getText().toString().trim());
                        person = i;
                        //Toast.makeText(ScoreCounterActivity.this,team.getPlayer(i).getName(),Toast.LENGTH_SHORT).show();
                    }
                }
                if (person >= 5) {
                    //设置名字
                    team.setName(editText.getText().toString().trim());
                    //设置人数
                    team.setPerson(person);

                    initTeam(team, direction);

                    dialog.dismiss();

//                    String string = editText.getText().toString().trim() + "\n";
//                    for (int i = 0; i < person; i++) {
//                        string = string + team.getPlayer(i).getNumber().toString() + " " + team.getPlayer(i).getName().toString() + "\n";
//                    }
//                    Toast.makeText(ScoreCounterActivity.this, string, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ScoreCounterActivity.this, "至少要5位球员", Toast.LENGTH_LONG).show();
                }

            }
        });
        view.findViewById(R.id.add_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initTeam(Team team, String direction) {
        if (direction.equals("left")) {
            leftFlag = true;
            //初始化场上球员
            for (int i = 0; i < 5; i++) {
                left_player_view[i] = i;
            }
            //初始化左边球队
            left_ball_team_name_TV.setText(team.getName());
            left_ball_team_TV.setText(team.getName());
            left_ball_team_name_TV.setClickable(false);

            left_team_top_player_number_TV.setText(team.getPlayer(0).getNumber());
            left_team_top_player_name_TV.setText(team.getPlayer(0).getName());

            left_team_middle_left_player_number_TV.setText(team.getPlayer(1).getNumber());
            left_team_middle_left_player_name_TV.setText(team.getPlayer(1).getName());
            left_team_middle_right_player_number_TV.setText(team.getPlayer(2).getNumber());
            left_team_middle_right_player_name_TV.setText(team.getPlayer(2).getName());

            left_team_bottom_left_player_number_TV.setText(team.getPlayer(3).getNumber());
            left_team_bottom_left_player_name_TV.setText(team.getPlayer(3).getName());
            left_team_bottom_right_player_number_TV.setText(team.getPlayer(4).getNumber());
            left_team_bottom_right_player_name_TV.setText(team.getPlayer(4).getName());

        } else {
            rightFlag = true;
            //初始化场上球员
            for (int i = 0; i < 5; i++) {
                right_player_view[i] = i;
            }
            //初始化右边球队
            right_ball_team_name_TV.setText(team.getName());
            right_ball_team_TV.setText(team.getName());
            right_ball_team_name_TV.setClickable(false);

            right_team_top_player_number_TV.setText(team.getPlayer(0).getNumber());
            right_team_top_player_name_TV.setText(team.getPlayer(0).getName());

            right_team_middle_left_player_number_TV.setText(team.getPlayer(1).getNumber());
            right_team_middle_left_player_name_TV.setText(team.getPlayer(1).getName());
            right_team_middle_right_player_number_TV.setText(team.getPlayer(2).getNumber());
            right_team_middle_right_player_name_TV.setText(team.getPlayer(2).getName());

            right_team_bottom_left_player_number_TV.setText(team.getPlayer(3).getNumber());
            right_team_bottom_left_player_name_TV.setText(team.getPlayer(3).getName());
            right_team_bottom_right_player_number_TV.setText(team.getPlayer(4).getNumber());
            right_team_bottom_right_player_name_TV.setText(team.getPlayer(4).getName());
        }
        reView();
    }

    private void reView() {
        left_team_score_TV.setText(String.valueOf(leftTeam.getScore()));
        right_team_score_TV.setText(String.valueOf(rightTeam.getScore()));
        left_team_pause_TV.setText(String.valueOf(leftTeam.getPause()));
        left_team_foul_TV.setText(String.valueOf(leftTeam.getFoul()));
        right_team_pause_TV.setText(String.valueOf(rightTeam.getPause()));
        right_team_foul_TV.setText(String.valueOf(rightTeam.getFoul()));
    }

    private void showGameStatistics() {
        if (isBeginGame) {
            Toast.makeText(ScoreCounterActivity.this, "统计分析", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScoreStatisticsActivity.class);
            intent.putExtra("leftTeam", leftTeam);
            intent.putExtra("rightTeam", rightTeam);
            startActivity(intent);
        } else {
            Toast.makeText(ScoreCounterActivity.this, "请先开始比赛", Toast.LENGTH_SHORT).show();
        }
    }

}


