package com.ethan.util.view;

import android.view.View;

public interface ListItemClickHelp {
    /**
     * 点击item条目中某个控件回调的方法
     *
     * @param position item在ListView中所处的位置
     * @param v        item中要点击的控件
     */
    void onClick(int position, View v);
}
