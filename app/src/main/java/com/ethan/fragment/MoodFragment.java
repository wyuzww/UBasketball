package com.ethan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.activity.moodfragment.MoodContentActivity;
import com.ethan.activity.moodfragment.WriteMoodActivity;
import com.ethan.activity.userfragment.MessageActivity;
import com.ethan.adapter.MoodAdapter;
import com.ethan.entity.Mood;
import com.ethan.entity.User;
import com.ethan.util.network.HttpClient;

import java.util.ArrayList;

public class MoodFragment extends Fragment implements View.OnClickListener {
    private TextView title_mood_all_TV;
    private TextView title_mood_mine_TV;
    private ImageView mood_search_IV;
    private ImageView mood_mine_msg_IV;
    private FloatingActionButton float_add_mood_BT;


    private ArrayList<Mood> moodArrayList = new ArrayList<>();
    private ArrayList<Integer> clockArrayListi = new ArrayList<>();
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

        bindView();

        initMoods();

        setAdapter();

    }

    private void bindView() {
        title_mood_all_TV = getActivity().findViewById(R.id.title_mood_all_id);
        title_mood_mine_TV = getActivity().findViewById(R.id.title_mood_mine_id);
        float_add_mood_BT = getActivity().findViewById(R.id.float_add_mood_button);
        mood_search_IV = getActivity().findViewById(R.id.mood_search_id);
        mood_mine_msg_IV = getActivity().findViewById(R.id.mood_mine_msg_id);


        title_mood_all_TV.setOnClickListener(this);
        title_mood_mine_TV.setOnClickListener(this);
        float_add_mood_BT.setOnClickListener(this);
        mood_search_IV.setOnClickListener(this);
        mood_mine_msg_IV.setOnClickListener(this);
    }

    private void initMoods() {
//        strings.add(new HttpClient().getURL()+ ( (2%2== 0) ? "userImage/13612250853.jpg" : "userImage/13612250852.jpg"));
        for (int i = 0; i < 9; i++) {
            strings = new ArrayList<>();
            User user = new User(i, String.valueOf(i), "1", "people " + String.valueOf(i), "123456", i % 2 == 0 ? "男" : "女", "1996-11-16", "Write the code , change the world!", "123456");

            for (int j = 9; j > i; j--)
                strings.add(new HttpClient().getURL() + ((j % 2 == 0) ? "userImage/13612250853.jpg" : "userImage/13612250852.jpg"));

            Mood mood = new Mood(i, user, "我是 " + String.valueOf(i) + ", 这是我的动态！欢迎点赞，谢谢！", "2018-10-11 03:4" + String.valueOf(i), i, i + 1, i, i + 1, strings);
            moodArrayList.add(mood);
        }
    }

    private void setAdapter() {
        MoodAdapter moodAdapter = new MoodAdapter(getActivity(), R.layout.moods_item, moodArrayList, clockArrayListi, loveArrayList);

        ListView listView = (ListView) getActivity().findViewById(R.id.mood_listview);
        listView.setAdapter(moodAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toMoodContent(moodArrayList.get(i), clockArrayListi, loveArrayList);
                Toast.makeText(getActivity(), moodArrayList.get(i).getUser().getUser_name(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_mood_all_id:
                Toast.makeText(getActivity(), "所有动态", Toast.LENGTH_SHORT).show();
                break;
            case R.id.title_mood_mine_id:
                Toast.makeText(getActivity(), "我的动态", Toast.LENGTH_SHORT).show();
                break;
            case R.id.float_add_mood_button:
                Toast.makeText(getActivity(), "添加动态", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), WriteMoodActivity.class));
                break;
            case R.id.mood_search_id:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mood_mine_msg_id:
                Toast.makeText(getActivity(), "msg", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
        }
    }

    private void toMoodContent(Mood mood, ArrayList<Integer> clockArrayListi, ArrayList<Integer> loveArrayList) {

        startActivity(new Intent(getActivity(), MoodContentActivity.class));
    }
}
