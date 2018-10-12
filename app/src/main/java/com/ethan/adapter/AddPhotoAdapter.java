package com.ethan.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.ethan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddPhotoAdapter extends BaseAdapter {
    private static final String IMG_ADD_TAG = "a";

    private int resourceId;
    private Context context;
    private List<Uri> uriLists;

    public AddPhotoAdapter(Context context, int resource, ArrayList<Uri> uriLists) {
        this.context = context;
        this.uriLists = uriLists;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return uriLists.size();
    }

    @Override
    public Object getItem(int position) {
        return uriLists.get(position);
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
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.photo_gridView_item_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Uri uri = uriLists.get(position);
        if (!uri.equals(Uri.parse(IMG_ADD_TAG))) {
            holder.checkBox.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(uri)
                    .centerCrop()
//                    .fit()
                    .resize(500, 500)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(holder.imageView);
        } else {
//            holder.imageView.setImageResource(R.drawable.pic);
            holder.checkBox.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(R.drawable.camera)
                    .placeholder(R.drawable.camera)
                    .error(R.drawable.camera)
                    .fit()
                    .into(holder.imageView);

        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                uriLists.remove(position);
                if (!uriLists.get(uriLists.size() - 1).equals(Uri.parse(IMG_ADD_TAG))) {
                    uriLists.add(Uri.parse(IMG_ADD_TAG));
                }
                notifyDataSetChanged();
            }
        });
        return convertView;

    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}
