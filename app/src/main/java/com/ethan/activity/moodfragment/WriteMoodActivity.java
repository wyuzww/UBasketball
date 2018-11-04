package com.ethan.activity.moodfragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ethan.R;
import com.ethan.activity.other.ImageDisplayActivity;
import com.ethan.activity.userfragment.login.LoginByPasswordActivity;
import com.ethan.adapter.AddPhotoAdapter;
import com.ethan.entity.User;
import com.ethan.util.Utils;
import com.ethan.util.manager.UriManager;
import com.ethan.util.network.HttpClient;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WriteMoodActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final int IMG_COUNT = 9;
    private static final String IMG_ADD_TAG = "a";
    private boolean isCamera = false;
    private ArrayList<Uri> uriLists;
    public ArrayList<Uri> uriArrayList;

    private AddPhotoAdapter addPhotoAdapter;


    private Button back_BT;
    private TextView to_sent_TV;
    private EditText mood_text_ET;
    private GridView gridView;

    //    private CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
//    private CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
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
        mood_text_ET = findViewById(R.id.mood_text_id);

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
                sentMood(new Utils().getUser(this));
//                Toast.makeText(WriteMoodActivity.this, "发送", Toast.LENGTH_SHORT).show();

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
        addPhotoAdapter.notifyDataSetChanged();
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

    private void sentMood(User user) {
        if (uriLists.size() > 1 || !mood_text_ET.getText().toString().trim().equals("")) {


            ArrayList<File> fileArrayList = new ArrayList<>();
            int amount = 0;
            final ProgressDialog login_Dialog = new ProgressDialog(this);
            login_Dialog.show();
            login_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            login_Dialog.setCancelable(false);
            login_Dialog.setCanceledOnTouchOutside(false);
            login_Dialog.setMessage("正在发送...");


            String url = "WriteMood";

            for (int i = 0; i < uriLists.size(); i++) {
                if (!uriLists.get(i).toString().equals(IMG_ADD_TAG)) {
                    amount++;
                    String path = UriManager.getPath_above19(this, uriLists.get(i));


                    File inFile = new File(path);

                    File outFile = new File(getExternalCacheDir(), user.getUser_id() + "" + System.currentTimeMillis() + ".jpg");
                    if (!outFile.exists() || !outFile.isFile()) {
                        try {
                            outFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    outFile = new Utils().compressImage(this, inFile, outFile);
                    fileArrayList.add(outFile);
//                Toast.makeText(WriteMoodActivity.this, inFile.getPath()+" ", Toast.LENGTH_SHORT).show();
//                Toast.makeText(WriteMoodActivity.this, inFile.getName()+" ", Toast.LENGTH_SHORT).show();
                }
            }

            MultipartBody.Builder body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            for (int i = 0; i < fileArrayList.size(); i++) {
                body.addFormDataPart("moodPhoto", fileArrayList.get(i).getName(), RequestBody.create(MediaType.parse("image/jpg"), fileArrayList.get(i)));
            }

            body.addFormDataPart("user_token", user.getUser_token());
            body.addFormDataPart("mood_text", mood_text_ET.getText().toString());
            body.addFormDataPart("mood_images_amount", String.valueOf(amount));
            body.addFormDataPart("mood_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            MultipartBody multipartBody = body.build();

            HttpClient httpClient = new HttpClient();
            httpClient.requestImage_Post(url, multipartBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WriteMoodActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                            }
                        });
                    } else if (e instanceof ConnectException) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(WriteMoodActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WriteMoodActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                            }
                        });
                    }
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        final String responseText = response.body().string();

                        final int code = JSONObject.parseObject(responseText).getInteger("code");
                        final String msg = JSONObject.parseObject(responseText).getString("msg");

                        if (msg.isEmpty() || msg.equals("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(WriteMoodActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                }
                            });
                        } else if (code == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    login_Dialog.dismiss();
                                    new AlertDialog.Builder(WriteMoodActivity.this)
                                            .setMessage(msg + ",请重新登录")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(WriteMoodActivity.this, LoginByPasswordActivity.class));
                                                    finish();
                                                }
                                            }).show();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(WriteMoodActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    login_Dialog.dismiss();
                                    if (code == 0) {
                                        deleteCache();
                                        startActivity(new Intent());
                                        finish();
                                    }
                                }
                            });
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WriteMoodActivity.this, "连接到服务器错误", Toast.LENGTH_SHORT).show();
                                login_Dialog.dismiss();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(WriteMoodActivity.this, "动态内容不能为空！", Toast.LENGTH_SHORT).show();
        }

    }

}
