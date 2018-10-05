package com.ethan.activity.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ethan.R;
import com.ethan.entity.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreStatisticsActivity extends Activity implements View.OnClickListener {
    private ListView player_statistics_LV;
    private TextView leftTeam_TV;
    private TextView rightTeam_TV;
    private TextView teamName_TV;
    private TextView teamScore_TV;
    private Button exit_BT;

    private Team leftTeam;
    private Team rightTeam;

    final private int before_textSize = 15;
    final private int after_textSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_score_statistics);

        getTeam();

        bindView();

        initData(leftTeam);
    }

    private void getTeam() {
        leftTeam = (Team) getIntent().getSerializableExtra("leftTeam");
        rightTeam = (Team) getIntent().getSerializableExtra("rightTeam");
    }

    private void bindView() {
        player_statistics_LV = findViewById(R.id.player_statistics_id);
        leftTeam_TV = findViewById(R.id.leftTeam);
        rightTeam_TV = findViewById(R.id.rightTeam);
        teamName_TV = findViewById(R.id.team_name);
        teamScore_TV = findViewById(R.id.team_score);
        exit_BT = findViewById(R.id.exit_id);

        leftTeam_TV.setOnClickListener(this);
        rightTeam_TV.setOnClickListener(this);
        exit_BT.setOnClickListener(this);
    }

    private void initData(Team team) {
        teamName_TV.setText(team.getName());
        teamScore_TV.setText(String.valueOf(team.getScore()));

        SimpleAdapter adapter = new SimpleAdapter(this, getData(team), R.layout.statistics_player_view,
                new String[]{"player", "score", "three", "two", "one", "assist", "backboard", "steal", "block", "foul"},
                new int[]{R.id.player, R.id.score, R.id.three, R.id.two, R.id.one, R.id.assist, R.id.backboard, R.id.steal, R.id.block, R.id.foul});

        player_statistics_LV.setAdapter(adapter);
    }

    private List<Map<String, Object>> getData(Team team) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        for (int i = 0; i < team.getPerson(); i++) {
            map = new HashMap<String, Object>();
            map.put("player", team.getPlayer(i).getNumber() + " " + team.getPlayer(i).getName());
            map.put("score", team.getPlayer(i).getScore());
            map.put("three", team.getPlayer(i).getThree_point());
            map.put("two", team.getPlayer(i).getTwo_point());
            map.put("one", team.getPlayer(i).getOne_point());
            map.put("assist", team.getPlayer(i).getAssist());
            map.put("backboard", team.getPlayer(i).getBackboard());
            map.put("steal", team.getPlayer(i).getSteal());
            map.put("block", team.getPlayer(i).getBlock());
            map.put("foul", team.getPlayer(i).getFoul());
            list.add(map);
        }

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftTeam:
//                Toast.makeText(this,"123",Toast.LENGTH_SHORT).show();
                rightTeam_TV.setTextSize(before_textSize);
                leftTeam_TV.setTextSize(after_textSize);
                initData(leftTeam);
                break;
            case R.id.rightTeam:
//                Toast.makeText(this,"456",Toast.LENGTH_SHORT).show();
                leftTeam_TV.setTextSize(before_textSize);
                rightTeam_TV.setTextSize(after_textSize);
                initData(rightTeam);
                break;
            case R.id.exit_id:
                finish();
                break;
        }
    }

}
