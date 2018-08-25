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

import java.util.ArrayList;
import java.util.List;

import com.ethan.activity.userfragment.UserInfoActivity;
import com.ethan.entity.Fruit;
import com.ethan.R;
import com.ethan.activity.login.LoginByPasswordActivity;

public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout user_info_head;
    private ImageView user_image_IV;
    private TextView user_name_TV;
    private TextView user_signature_TV;
    private Boolean isLogined;
    private SharedPreferences user_info_preferences;
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUser();


        //       initUser();
//        FruitAdapter fruitAdapter = new FruitAdapter(getActivity(), R.layout.fruit_item, fruitList);
//        ListView listView = (ListView) getActivity().findViewById(R.id.user_listview);
//        listView.setAdapter(fruitAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Fruit fruit = fruitList.get(i);
//                Toast.makeText(getActivity(), fruit.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    private void initUser() {

        user_info_head = (LinearLayout) getActivity().findViewById(R.id.user_info_head);
        user_image_IV = (ImageView) getActivity().findViewById(R.id.user_image);
        user_name_TV = (TextView) getActivity().findViewById(R.id.user_name);
        user_signature_TV = (TextView) getActivity().findViewById(R.id.user_signature);
        user_info_head.setOnClickListener(this);


        loadUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_head:
                user_info_head();
//                startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
                break;
        }
    }

    private void loadUser() {
        user_info_preferences = getContext().getSharedPreferences("UserInfo", 0);
        isLogined = user_info_preferences.getBoolean("isLogined", false);
        if (isLogined) {
            String user_name = user_info_preferences.getString("user_name", "");
            String user_signature = user_info_preferences.getString("user_signature", "未设置个性签名...");
            if (user_signature.equals("")) {
                user_signature = "未设置个性签名...";
            }
            user_name_TV.setText(user_name);
            user_signature_TV.setText(user_signature);
        } else {
            user_name_TV.setText("未登录");
            user_signature_TV.setText("请先登录...");
        }

    }

    private void user_info_head() {
        if (isLogined) {
            Toast.makeText(getActivity(), "已经登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
