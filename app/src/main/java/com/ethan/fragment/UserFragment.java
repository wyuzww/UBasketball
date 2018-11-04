package com.ethan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.activity.game.ScoreCounterActivity;
import com.ethan.activity.userfragment.AboutActivity;
import com.ethan.activity.userfragment.ClockActivity;
import com.ethan.activity.userfragment.FollowActivity;
import com.ethan.activity.userfragment.HelpActivity;
import com.ethan.activity.userfragment.MessageActivity;
import com.ethan.activity.userfragment.SecurityActivity;
import com.ethan.activity.userfragment.SettingActivity;
import com.ethan.activity.userfragment.login.LoginByPasswordActivity;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.activity.userfragment.user.UserInfoActivity;
import com.ethan.entity.User;
import com.ethan.util.Utils;

public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout user_info_head_LL;
    private LinearLayout score_counter_LL;
    private LinearLayout about_LL;
    private LinearLayout setting_LL;
    private LinearLayout clock_LL;
    private LinearLayout message_LL;
    private LinearLayout my_homepage_LL;
    private LinearLayout follow_LL;
    private LinearLayout video_LL;
    private LinearLayout security_LL;
    private LinearLayout help_Ll;


    private User user;
    private ImageView user_image_IV;
    private ImageView user_sex_TV;
    private TextView user_name_TV;
    private TextView user_signature_TV;
    private Boolean isLogined;
    private SharedPreferences user_info_preferences;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        bindView();

        initUser();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    private void initUser() {

        loadUser();

    }

    private void bindView() {

        user_info_head_LL = (LinearLayout) getActivity().findViewById(R.id.user_info_head);
        score_counter_LL = (LinearLayout) getActivity().findViewById(R.id.score_counter_id);
        about_LL = (LinearLayout) getActivity().findViewById(R.id.about_id);
        setting_LL = getActivity().findViewById(R.id.setting_id);
        clock_LL = getActivity().findViewById(R.id.clock_id);
        message_LL = getActivity().findViewById(R.id.msg_id);
        my_homepage_LL = getActivity().findViewById(R.id.my_homepage_id);
        follow_LL = getActivity().findViewById(R.id.follow_id);
        video_LL = getActivity().findViewById(R.id.video_id);
        security_LL = getActivity().findViewById(R.id.security_id);
        help_Ll = getActivity().findViewById(R.id.help_id);
        user_image_IV = (ImageView) getActivity().findViewById(R.id.user_image_id);
        user_name_TV = (TextView) getActivity().findViewById(R.id.user_name_id);
        user_signature_TV = (TextView) getActivity().findViewById(R.id.user_signature_id);
        user_sex_TV = getActivity().findViewById(R.id.user_sex_id);


        user_info_head_LL.setOnClickListener(this);
        score_counter_LL.setOnClickListener(this);
        about_LL.setOnClickListener(this);
        setting_LL.setOnClickListener(this);
        clock_LL.setOnClickListener(this);
        message_LL.setOnClickListener(this);
        my_homepage_LL.setOnClickListener(this);
        follow_LL.setOnClickListener(this);
        video_LL.setOnClickListener(this);
        security_LL.setOnClickListener(this);
        help_Ll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_head:
                user_info_head();
//                startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
                break;
            case R.id.score_counter_id:
                to_Score_Counter();
                break;
            case R.id.about_id:
//                Toast.makeText(getActivity(), "关于", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.clock_id:
//                Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                if (new Utils().isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), ClockActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.msg_id:
//                Toast.makeText(getActivity(), "消息", Toast.LENGTH_SHORT).show();
                if (new Utils().isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_homepage_id:
//                Toast.makeText(getActivity(), "我的主页", Toast.LENGTH_SHORT).show();
                if (new Utils().isLogined(getActivity())) {
                    toHomePage();
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(getActivity(), UserHomePageActivity.class));
                break;
            case R.id.follow_id:
//                Toast.makeText(getActivity(), "关注", Toast.LENGTH_SHORT).show();
                if (new Utils().isLogined(getActivity())) {
                    startActivity(new Intent(getActivity(), FollowActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.video_id:
                Toast.makeText(getActivity(), "暂不支持，程序猿正在赶工...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.security_id:
//                Toast.makeText(getActivity(), "安全", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SecurityActivity.class));
                break;
            case R.id.setting_id:
//                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.help_id:
//                Toast.makeText(getActivity(), "帮助", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;

        }
    }

    private void loadUser() {
        user_info_preferences = getContext().getSharedPreferences("UserInfo", 0);
        isLogined = user_info_preferences.getBoolean("isLogined", false);
        if (isLogined) {

            String user_number = user_info_preferences.getString("user_number", "user_temp");
            String user_image = user_info_preferences.getString("user_image", "userImage/" + user_number + ".jpg");
            String user_name = user_info_preferences.getString("user_name", "");
            String user_signature = user_info_preferences.getString("user_signature", "未设置个性签名...");
            String user_sex = user_info_preferences.getString("user_sex", "");

            user = new Utils().getUser(getActivity());
            new Utils().findUserImage(user_image, user_number, getActivity(), user_image_IV);

            if (user_signature.equals("")) {
                user_signature = "未设置个性签名...";
            }
            user_name_TV.setText(user_name);
            user_signature_TV.setText(user_signature);
            if (user_sex.equals("女")) {
                user_sex_TV.setVisibility(View.VISIBLE);
                user_sex_TV.setImageResource(R.drawable.ic_woman);
            } else if (user_sex.equals("男")) {
                user_sex_TV.setVisibility(View.VISIBLE);
                user_sex_TV.setImageResource(R.drawable.ic_man);
            } else {
                user_sex_TV.setVisibility(View.GONE);
            }
        } else {
            user_image_IV.setImageResource(R.drawable.ic_user_icon);
            user_name_TV.setText("未登录");
            user_signature_TV.setText("请先登录...");
            user_sex_TV.setVisibility(View.GONE);
        }

    }

    private void user_info_head() {
        if (isLogined) {
//            Toast.makeText(getActivity(), "已经登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
        }
    }

    private void toHomePage() {
        if (isLogined) {
            Intent intent = new Intent(getActivity(), UserHomePageActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "请先登录~", Toast.LENGTH_SHORT).show();
        }

    }

    private void to_Score_Counter() {
        Intent intent = new Intent(getActivity(), ScoreCounterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
