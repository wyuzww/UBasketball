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
import com.ethan.activity.userfragment.login.LoginByPasswordActivity;
import com.ethan.activity.userfragment.AboutActivity;
import com.ethan.activity.userfragment.ClockActivity;
import com.ethan.activity.userfragment.FollowActivity;
import com.ethan.activity.userfragment.HelpActivity;
import com.ethan.activity.userfragment.MessageActivity;
import com.ethan.activity.userfragment.SecurityActivity;
import com.ethan.activity.userfragment.SettingActivity;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.activity.userfragment.user.UserInfoActivity;
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


    private ImageView user_image_IV;
    private TextView user_name_TV;
    private TextView user_signature_TV;
    private Boolean isLogined;
    private SharedPreferences user_info_preferences;
//    private List<Fruit> fruitList = new ArrayList<>();

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
        user_image_IV = (ImageView) getActivity().findViewById(R.id.user_image);
        user_name_TV = (TextView) getActivity().findViewById(R.id.user_name);
        user_signature_TV = (TextView) getActivity().findViewById(R.id.user_signature);


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
                startActivity(new Intent(getActivity(), ClockActivity.class));
                break;
            case R.id.msg_id:
//                Toast.makeText(getActivity(), "消息", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.my_homepage_id:
//                Toast.makeText(getActivity(), "我的主页", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), UserHomePageActivity.class));
                break;
            case R.id.follow_id:
//                Toast.makeText(getActivity(), "关注", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), FollowActivity.class));
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

//            final File file = new File(getActivity().getExternalFilesDir("userImage"), user_number + ".jpg");
//
//            //user_image_IV.setImageBitmap();//头像
//            if (file.exists()) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Picasso.with(getActivity()).invalidate(file);
//                        Picasso.with(getActivity()).load(file)
//                                .into(user_image_IV);
//                    }
//                });
//
//            } else {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {//http://192.168.43.196/UBasketball/     "http://wyuzww.nat123.net/UBasketball/userImage/13612250853.jpg"
//                        Picasso.with(getActivity()).invalidate("http://192.168.43.196/UBasketball/userImage/13612250853.jpg");
//                        Picasso.with(getActivity()).load("http://192.168.43.196/UBasketball/userImage/13612250853.jpg")
//                                .error(R.mipmap.ic_logo)
//                                .placeholder(R.mipmap.ic_logo)
//                                .into(user_image_IV);
//                    }
//                });
//            }


            new Utils().findUserImage(user_image, user_number, getActivity(), user_image_IV);

            if (user_signature.equals("")) {
                user_signature = "未设置个性签名...";
            }
            user_name_TV.setText(user_name);
            user_signature_TV.setText(user_signature);
        } else {
            user_image_IV.setImageResource(R.drawable.ic_user_icon);
            user_name_TV.setText("未登录");
            user_signature_TV.setText("请先登录...");
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
