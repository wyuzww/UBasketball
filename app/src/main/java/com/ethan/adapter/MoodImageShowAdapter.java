package com.ethan.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ethan.R;
import com.ethan.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoodImageShowAdapter extends BaseAdapter {

    private int resourceId;
    private Context context;
    private ArrayList<String> imageList;

    public MoodImageShowAdapter(Context context, int resource, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo_gridView_item_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String url = imageList.get(position);
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_basketball)
                .error(R.drawable.ic_basketball)
//                .fit()
                .centerCrop()
                .resize(800, 800)

//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, String.valueOf(imageList.size()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ImageDisplayActivity.class);
//                intent.putExtra("image_uri", url);
//                context.startActivity(intent);
                new Utils().toShowImage(context, url);
            }
        });


        return convertView;

    }

    private class ViewHolder {
        ImageView imageView;
    }
}
