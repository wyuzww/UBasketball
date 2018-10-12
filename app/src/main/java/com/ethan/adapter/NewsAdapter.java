package com.ethan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.R;
import com.ethan.entity.News;
import com.ethan.util.picasso.PicassoTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceId;
    private Context context;
    private List<News> newsList;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> newsList) {
        super(context, resource, newsList);
        this.context = context;
        this.newsList = newsList;
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.news_title_TV = (TextView) view.findViewById(R.id.news_title_id);
            viewHolder.news_pic1_IV = (ImageView) view.findViewById(R.id.news_pic1_id);
            viewHolder.news_pic2_IV = (ImageView) view.findViewById(R.id.news_pic2_id);
            viewHolder.news_pic3_IV = (ImageView) view.findViewById(R.id.news_pic3_id);
            viewHolder.news_author_TV = (TextView) view.findViewById(R.id.news_author_id);
            viewHolder.news_date_TV = (TextView) view.findViewById(R.id.news_date_id);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.news_title_TV.setText(newsList.get(position).getNews_title());
        Picasso.with(context).load(newsList.get(position).getNews_pic_url1())
                .transform(new PicassoTransformation(context, 3))
                .placeholder(R.drawable.ic_basketball)
                .error(R.drawable.ic_basketball)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.news_pic1_IV);
        Picasso.with(context).load(newsList.get(position).getNews_pic_url2())
                .transform(new PicassoTransformation(context, 3))
                .placeholder(R.drawable.ic_basketball)
                .error(R.drawable.ic_basketball)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.news_pic2_IV);
        Picasso.with(context).load(newsList.get(position).getNews_pic_url3())
                .transform(new PicassoTransformation(context, 3))
                .placeholder(R.drawable.ic_basketball)
                .error(R.drawable.ic_basketball)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.news_pic3_IV);
        viewHolder.news_author_TV.setText(newsList.get(position).getNews_author());
        viewHolder.news_date_TV.setText(newsList.get(position).getNews_date());
        return view;
    }

    private class ViewHolder {
        TextView news_title_TV;
        ImageView news_pic1_IV;
        ImageView news_pic2_IV;
        ImageView news_pic3_IV;
        TextView news_author_TV;
        TextView news_date_TV;
    }
}
