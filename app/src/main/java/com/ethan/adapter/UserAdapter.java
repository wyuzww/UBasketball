package com.ethan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.R;
import com.ethan.activity.userfragment.user.UserHomePageActivity;
import com.ethan.entity.User;
import com.ethan.util.view.ListItemClickHelp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {
    private int resourceId;
    private Context context;
    private ListItemClickHelp callback;
    private ArrayList<User> userArrayList;

    public UserAdapter(@NonNull Context context, int resource, ListItemClickHelp callback, @NonNull ArrayList<User> userArrayList) {
        super(context, resource, userArrayList);
        this.context = context;
        this.resourceId = resource;
        this.callback = callback;
        this.userArrayList = userArrayList;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.user_image_IV = (ImageView) view.findViewById(R.id.user_image_id);
            viewHolder.user_name_TV = (TextView) view.findViewById(R.id.user_name_id);
            viewHolder.user_sex_IV = (ImageView) view.findViewById(R.id.user_sex_id);
            viewHolder.user_signature_TV = (TextView) view.findViewById(R.id.user_signature_id);
            viewHolder.to_follow_IV = (ImageView) view.findViewById(R.id.to_follow_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //初始化头像
        Picasso.with(context).load(userArrayList.get(position).getUser_image())
                .error(R.drawable.ic_user_icon)
                .placeholder(R.drawable.ic_user_icon)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.user_image_IV);

        //初始化昵称、性别
        viewHolder.user_name_TV.setText(userArrayList.get(position).getUser_name());

        if (userArrayList.get(position).getUser_sex().equals("男")) {
            viewHolder.user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.user_sex_IV.setImageResource(R.drawable.ic_man);
        } else if (userArrayList.get(position).getUser_sex().equals("女")) {
            viewHolder.user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.user_sex_IV.setImageResource(R.drawable.ic_woman);
        } else {
            viewHolder.user_sex_IV.setVisibility(View.GONE);
        }
        //
        viewHolder.user_signature_TV.setText(userArrayList.get(position).getUser_signature());
        viewHolder.to_follow_IV.setImageResource(R.drawable.ic_cfollow);

        viewHolder.user_image_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserHomePageActivity.class));
//                Toast.makeText(context, "用户 " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.to_follow_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "关注用户 " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                callback.onClick(position, v);
            }
        });

        return view;
    }

    private class ViewHolder {
        ImageView user_image_IV;
        TextView user_name_TV;
        ImageView user_sex_IV;
        TextView user_signature_TV;
        ImageView to_follow_IV;
    }
}
