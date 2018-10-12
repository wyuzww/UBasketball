package com.ethan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.entity.Mood;
import com.ethan.util.network.HttpClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoodAdapter extends ArrayAdapter<Mood> {
    private int resourceId;
    private Context context;
    private ArrayList<Mood> moodArrayList;
    private ArrayList<Integer> clockArrayListi;
    private ArrayList<Integer> loveArrayList;
//    private int position;
//    private boolean isLove = false;

    public MoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Mood> moodArrayList, ArrayList<Integer> clockArrayListi, ArrayList<Integer> loveArrayListi) {
        super(context, resource, moodArrayList);
        this.context = context;
        this.resourceId = resource;
        this.moodArrayList = moodArrayList;
        this.clockArrayListi = clockArrayListi;
        this.loveArrayList = loveArrayListi;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mood_user_image_IV = (ImageView) view.findViewById(R.id.mood_user_image_id);
            viewHolder.mood_user_name_TV = (TextView) view.findViewById(R.id.mood_user_name_id);
            viewHolder.mood_user_sex_IV = (ImageView) view.findViewById(R.id.mood_user_sex_id);
            viewHolder.mood_time_TV = (TextView) view.findViewById(R.id.mood_time_id);
            viewHolder.mood_text_TV = (TextView) view.findViewById(R.id.mood_text_id);
            viewHolder.mood_image_GV = (GridView) view.findViewById(R.id.mood_image_gv_id);
            viewHolder.mood_clock_TV = (TextView) view.findViewById(R.id.mood_clock_id);
            viewHolder.mood_comment_TV = (TextView) view.findViewById(R.id.mood_comment_id);
            viewHolder.mood_love_TV = (TextView) view.findViewById(R.id.mood_love_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //初始化头像
        Picasso.with(context).load(position % 2 == 0 ? new HttpClient().getURL() + "userImage/13612250853.jpg" : new HttpClient().getURL() + "userImage/13612250852.jpg")
                .error(R.drawable.ic_user_icon)
                .placeholder(R.drawable.ic_user_icon)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.mood_user_image_IV);

        //初始化昵称、性别
        viewHolder.mood_user_name_TV.setText(moodArrayList.get(position).getUser().getUser_name());

        if (moodArrayList.get(position).getUser().getUser_sex().equals("男")) {
            viewHolder.mood_user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.mood_user_sex_IV.setImageResource(R.drawable.ic_man);
        } else if (moodArrayList.get(position).getUser().getUser_sex().equals("女")) {
            viewHolder.mood_user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.mood_user_sex_IV.setImageResource(R.drawable.ic_woman);
        } else {
            viewHolder.mood_user_sex_IV.setVisibility(View.GONE);
        }
        //初始化时间、内容
        viewHolder.mood_time_TV.setText(moodArrayList.get(position).getMood_time());
        viewHolder.mood_text_TV.setText(moodArrayList.get(position).getMood_text());

        //初始化照片gridview
        viewHolder.mood_image_GV.setAdapter(new MoodImageShowAdapter(context, R.layout.mood_image_show_gv_items, moodArrayList.get(position).getMood_images_url()));
//        viewHolder.mood_image_GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,"image "+String.valueOf(position),Toast.LENGTH_SHORT).show();
//            }
//        });


        //初始化收藏、评论、点赞
        if (moodArrayList.get(position).getMood_clocks_amount() > 0) {
            viewHolder.mood_clock_TV.setText(String.valueOf(moodArrayList.get(position).getMood_clocks_amount()));
        } else {
            viewHolder.mood_clock_TV.setText("收藏");
        }
        if (moodArrayList.get(position).getMood_comments_amount() > 0) {
            viewHolder.mood_comment_TV.setText(String.valueOf(moodArrayList.get(position).getMood_comments_amount()));
        } else {
            viewHolder.mood_comment_TV.setText("评论");
        }
        if (moodArrayList.get(position).getMood_loves_amount() > 0) {
            viewHolder.mood_love_TV.setText(String.valueOf(moodArrayList.get(position).getMood_loves_amount()));
        } else {
            viewHolder.mood_love_TV.setText("点赞");
        }
        if (!clockArrayListi.contains(moodArrayList.get(position).getMood_id())) {
            viewHolder.mood_clock_TV.setSelected(false);
        } else {
            viewHolder.mood_clock_TV.setSelected(true);
        }
        if (!loveArrayList.contains(moodArrayList.get(position).getMood_id())) {
            viewHolder.mood_love_TV.setSelected(false);
        } else {
            viewHolder.mood_love_TV.setSelected(true);
        }


        viewHolder.mood_user_image_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserHomePageActivity.class));
                Toast.makeText(context, "用户 " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.mood_clock_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(moodArrayList.get(position).getMood_images_url().size()), Toast.LENGTH_SHORT).show();
                if (!v.isSelected()) {
                    clockArrayListi.add(moodArrayList.get(position).getMood_id());
                    moodArrayList.get(position).setMood_clocks_amount(moodArrayList.get(position).getMood_clocks_amount() + 1);
                } else {
                    clockArrayListi.remove(clockArrayListi.lastIndexOf(moodArrayList.get(position).getMood_id()));
                    moodArrayList.get(position).setMood_clocks_amount(moodArrayList.get(position).getMood_clocks_amount() - 1);
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.mood_comment_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"评论",Toast.LENGTH_SHORT).show();
                moodArrayList.get(position).setMood_comments_amount(moodArrayList.get(position).getMood_comments_amount() + 1);
                notifyDataSetChanged();
            }
        });
        viewHolder.mood_love_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"点赞",Toast.LENGTH_SHORT).show();
                if (!v.isSelected()) {
                    loveArrayList.add(moodArrayList.get(position).getMood_id());
                    moodArrayList.get(position).setMood_loves_amount(moodArrayList.get(position).getMood_loves_amount() + 1);
                } else {
                    loveArrayList.remove(loveArrayList.lastIndexOf(moodArrayList.get(position).getMood_id()));
                    moodArrayList.get(position).setMood_loves_amount(moodArrayList.get(position).getMood_loves_amount() - 1);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private class ViewHolder {
        ImageView mood_user_image_IV;
        TextView mood_user_name_TV;
        ImageView mood_user_sex_IV;
        TextView mood_time_TV;
        TextView mood_text_TV;
        GridView mood_image_GV;
        TextView mood_clock_TV;
        TextView mood_comment_TV;
        TextView mood_love_TV;
    }
}
