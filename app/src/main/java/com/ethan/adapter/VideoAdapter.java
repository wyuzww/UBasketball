package com.ethan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ethan.R;
import com.ethan.entity.Video;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoAdapter extends ArrayAdapter<Video> {
    private ArrayList<Video> videos;
    private Context mContext;
    private int resource;
    private int pager = -1;


    public VideoAdapter(Context context, int resource, ArrayList<Video> videos) {
        super(context, resource, videos);
        this.mContext = context;
        this.videos = videos;
        this.resource = resource;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return pager == -1 ? videos.size() : 4;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(resource, parent, false);
            holder.mJCVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (pager == -1) {
            holder.mJCVideoPlayerStandard.setUp(
                    videos.get(position).getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    videos.get(position).getVideo_title());
            Log.e("TAG", "setUp" + position);
            Picasso.with(convertView.getContext())
                    .load(videos.get(position).getVideo_pic())
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(holder.mJCVideoPlayerStandard.thumbImageView);
        }

        return convertView;
    }

    private class ViewHolder {
        JCVideoPlayerStandard mJCVideoPlayerStandard;
    }
}
