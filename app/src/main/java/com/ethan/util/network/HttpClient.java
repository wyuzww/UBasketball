package com.ethan.util.network;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {
    String URL1 = "http://192.168.43.196/UBasketball/";//手机开热点测试ip
    String URL2 = "http://wyuzww.nat123.net/UBasketball/";//80端口映射ip
    String URL3 = "http://wyuzww.nat123.cc:17205/UBasketball/";//非80端口映射ip
    String URL = URL1;


    public void request_Get(String url, Callback callback) {
        //       String url = "wyuzww.nat123.net/UBasketball/login?user_number="+phoneNummber+"&user_password="+password;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(URL + url)
//                .cacheControl(new CacheControl.Builder().maxAge(0,TimeUnit.SECONDS).build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    public void request_Post(String url, FormBody formBody, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(URL + url)
                .post(formBody)
//                .cacheControl(new CacheControl.Builder().maxAge(0,TimeUnit.SECONDS).build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
