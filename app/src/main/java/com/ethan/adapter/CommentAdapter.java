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
import com.ethan.entity.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private int resourceId;
    private Context context;
    private ArrayList<Comment> commentArrayList;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Comment> commentArrayList) {
        super(context, resource, commentArrayList);
        this.context = context;
        this.resourceId = resource;
        this.commentArrayList = commentArrayList;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.comment_user_image_IV = (ImageView) view.findViewById(R.id.comment_user_image_id);
            viewHolder.comment_user_name_TV = (TextView) view.findViewById(R.id.comment_user_name_id);
            viewHolder.comment_user_sex_IV = (ImageView) view.findViewById(R.id.comment_user_sex_id);
            viewHolder.comment_time_TV = (TextView) view.findViewById(R.id.comment_time_id);
            viewHolder.comment_text_TV = (TextView) view.findViewById(R.id.comment_text_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //初始化头像
        Picasso.with(context).load(commentArrayList.get(position).getUser().getUser_image())
                .error(R.drawable.ic_user_icon)
                .placeholder(R.drawable.ic_user_icon)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.comment_user_image_IV);

        //初始化昵称、性别
        viewHolder.comment_user_name_TV.setText(commentArrayList.get(position).getUser().getUser_name());

        if (commentArrayList.get(position).getUser().getUser_sex().equals("男")) {
            viewHolder.comment_user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.comment_user_sex_IV.setImageResource(R.drawable.ic_man);
        } else if (commentArrayList.get(position).getUser().getUser_sex().equals("女")) {
            viewHolder.comment_user_sex_IV.setVisibility(View.VISIBLE);
            viewHolder.comment_user_sex_IV.setImageResource(R.drawable.ic_woman);
        } else {
            viewHolder.comment_user_sex_IV.setVisibility(View.GONE);
        }
        //初始化时间、内容
        viewHolder.comment_time_TV.setText(commentArrayList.get(position).getComment_time());
        viewHolder.comment_text_TV.setText(commentArrayList.get(position).getComment_text());

        viewHolder.comment_user_image_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserHomePageActivity.class);
                intent.putExtra("user", commentArrayList.get(position).getUser());
                context.startActivity(intent);
//                context.startActivity(new Intent(context, UserHomePageActivity.class));
//                Toast.makeText(context, "用户 " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class ViewHolder {
        ImageView comment_user_image_IV;
        TextView comment_user_name_TV;
        ImageView comment_user_sex_IV;
        TextView comment_time_TV;
        TextView comment_text_TV;
    }
}
