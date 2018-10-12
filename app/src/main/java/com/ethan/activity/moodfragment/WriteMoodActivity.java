package com.ethan.activity.moodfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.R;
import com.ethan.activity.other.ImageDisplayActivity;
import com.ethan.adapter.AddPhotoAdapter;
import com.ethan.util.Utils;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;

public class WriteMoodActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final int IMG_COUNT = 9;
    private static final String IMG_ADD_TAG = "a";
    private boolean isCamera = false;
    private ArrayList<Uri> uriLists;
    public ArrayList<Uri> uriArrayList;

    private AddPhotoAdapter addPhotoAdapter;


    private Button back_BT;
    private TextView to_sent_TV;
    private GridView gridView;


    //    LubanOptions option=new LubanOptions.Builder()
//
////            .putGear(Luban.CUSTOM_GEAR)
//            .setMaxHeight(800)
//            .setMaxWidth(800)
//            .setMaxSize(50*1024)
//            .create();
//    CompressConfig config=CompressConfig.ofLuban(option);
    private CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
    private CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
    private Uri photoUri;
    private ArrayList<TImage> photos;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mood);

        new Utils().setFullScreen(getWindow());

        bindView();

        setAdpter();

    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);
        to_sent_TV = findViewById(R.id.to_sent_id);
        gridView = findViewById(R.id.add_photo_gv_id);

        uriLists = new ArrayList<>();
        uriArrayList = new ArrayList<>();
        uriLists.add((Uri.parse(IMG_ADD_TAG)));


        back_BT.setOnClickListener(this);
        to_sent_TV.setOnClickListener(this);
    }

    private void setAdpter() {

        addPhotoAdapter = new AddPhotoAdapter(WriteMoodActivity.this, R.layout.add_photo_gv_items, uriLists);
        gridView.setAdapter(addPhotoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                addPhotoAdapter.notifyDataSetChanged();
                if (uriLists.size() < 10 && uriLists.get(position).equals(Uri.parse(IMG_ADD_TAG))) {
//                    Toast.makeText(WriteMoodActivity.this, "打开" + String.valueOf(uriLists.size()), Toast.LENGTH_SHORT).show();
                    add_photo(IMG_COUNT - uriLists.size() + 1);

                } else {
                    seePhoto(uriLists.get(position));
                    Toast.makeText(WriteMoodActivity.this, "放大" + String.valueOf(uriLists.size()), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(WriteMoodActivity.this, "##  " + String.valueOf(uriLists.size()), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.to_sent_id:
                Toast.makeText(WriteMoodActivity.this, "发送", Toast.LENGTH_SHORT).show();
                deleteCache();
                break;
        }
    }

    public void add_photo(final int limit) {
        new AlertDialog.Builder(WriteMoodActivity.this)
                .setTitle("选择照片")
                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        photoUri = Uri.fromFile(file);
                        if (which == 0) {

                            isCamera = true;
//                            getTakePhoto().onEnableCompress(compressConfig,true);
                            getTakePhoto().onPickFromCapture(photoUri);

                        } else if (which == 1) {

                            //getTakePhoto().onEnableCompress(compressConfig,true);
                            getTakePhoto().onPickMultiple(limit);

                        }
                    }
                }).create().show();

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        photos = result.getImages();

        if (photos != null && !photos.isEmpty()) {

            if (isCamera) {
                uriArrayList.add(photoUri);
            } else {
                for (int i = 0; i < photos.size(); i++) {
//                Toast.makeText(WriteMoodActivity.this,photos.get(i).getOriginalPath(),Toast.LENGTH_SHORT).show();

                    photoUri = Uri.fromFile(new File(photos.get(i).getOriginalPath()));
                    uriArrayList.add(photoUri);

                }
            }
            isCamera = false;

        } else {
            return;
        }
        updatePhoto();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(WriteMoodActivity.this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        Toast.makeText(WriteMoodActivity.this, "cancel", Toast.LENGTH_SHORT).show();
    }

    private void updatePhoto() {
        uriLists.remove(uriLists.size() - 1);
        for (int i = 0; i < uriArrayList.size(); i++) {
            uriLists.add(uriArrayList.get(i));
        }
        uriArrayList.clear();
        if (uriLists.size() < IMG_COUNT) {
            uriLists.add((Uri.parse(IMG_ADD_TAG)));
        }
        addPhotoAdapter.notifyDataSetChanged();
    }

    private void seePhoto(Uri uri) {

        Intent intent = new Intent(WriteMoodActivity.this, ImageDisplayActivity.class);
        intent.putExtra("image_uri", String.valueOf(uri));
        startActivity(intent);

    }

    private void deleteCache() {
        File directory = getExternalCacheDir();
        if (directory.exists() && directory != null && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

}
