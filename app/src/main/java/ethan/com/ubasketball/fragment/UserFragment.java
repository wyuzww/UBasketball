package ethan.com.ubasketball.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ethan.com.ubasketball.login.LoginByPasswordActivity;
import ethan.com.ubasketball.R;
import ethan.com.ubasketball.adapter.FruitAdapter;
import ethan.com.ubasketball.entity.Fruit;

public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView userTologin;
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userTologin = (TextView) getActivity().findViewById(R.id.user_id);
        userTologin.setOnClickListener(this);

        initFruits();
        FruitAdapter fruitAdapter = new FruitAdapter(getActivity(), R.layout.fruit_item, fruitList);
        ListView listView = (ListView) getActivity().findViewById(R.id.user_listview);
        listView.setAdapter(fruitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit fruit = fruitList.get(i);
                Toast.makeText(getActivity(), fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    private void initFruits() {
        for (int i = 0; i < 4; i++) {
            Fruit apple = new Fruit("Apple", R.drawable.ic_cmain);
            fruitList.add(apple);
            Fruit banana = new Fruit("Banana", R.drawable.ic_cclock);
            fruitList.add(banana);
            Fruit orange = new Fruit("Orange", R.drawable.ic_csearch);
            fruitList.add(orange);
            Fruit watermelon = new Fruit("Watermelon", R.drawable.ic_cuser);
            fruitList.add(watermelon);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_id:
                startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
                break;
        }
    }


}
