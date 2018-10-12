package com.ethan.activity.other;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ethan.R;
import com.ethan.util.picasso.PicassoTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity implements View.OnClickListener {
    private ImageView large_image_IV;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        bindView();

        getImage();

        showImage(image_uri);


    }

    private void bindView() {
        large_image_IV = findViewById(R.id.large_image_id);

        large_image_IV.setOnClickListener(this);
    }

    private void getImage() {
        image_uri = Uri.parse(getIntent().getStringExtra("image_uri"));
    }

    private void showImage(Uri uri) {
//        Picasso.with(this).invalidate(uri);
        Picasso.with(this).load(uri)
//                .fit()
//                .centerCrop()
                .transform(new PicassoTransformation(this, 1))
                .error(R.drawable.ic_loading_fail)
                .placeholder(R.drawable.ic_loading)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(large_image_IV);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
