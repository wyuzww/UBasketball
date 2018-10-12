package com.ethan.util.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.squareup.picasso.Transformation;

public class PicassoTransformation implements Transformation {

    int screenWidth;
    double targetWidth;

    /**
     * @param context 为了得到contenxt对象获得屏幕宽度
     * @param aver    根据屏幕宽度进行的等分
     */
    public PicassoTransformation(Context context, int aver) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        //获得屏幕宽度
        screenWidth = displayMetrics.widthPixels;
        targetWidth = screenWidth / aver;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return bitmap;
        }
        //得到图片宽高比,每个参数必须强转成double型
        double ratio = (double) bitmap.getWidth() / (double) bitmap.getHeight();

        Bitmap bitmapResult = null;
        if (bitmap != null) {
            bitmapResult = Bitmap.createScaledBitmap(bitmap, (int) (targetWidth + 0.5), (int) ((targetWidth / ratio) + 0.5), false);
        }
        if (bitmap != bitmapResult) {
            bitmap.recycle();
        }

        return bitmapResult;
    }

    @Override
    public String key() {
        return "transformation" + screenWidth;
    }
}