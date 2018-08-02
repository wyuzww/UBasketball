package ethan.com.ubasketball.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ethan.com.ubasketball.R;
import ethan.com.ubasketball.adapter.FruitAdapter;
import ethan.com.ubasketball.entity.Fruit;

public class ClockFragment extends Fragment {
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFruits();
        FruitAdapter fruitAdapter = new FruitAdapter(getActivity(), R.layout.fruit_item, fruitList);

        ListView listView = (ListView) getActivity().findViewById(R.id.clock_listview);
        listView.setAdapter(fruitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit fruit = fruitList.get(i);
                Toast.makeText(getActivity(), fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });

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


}
