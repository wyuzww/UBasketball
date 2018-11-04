package com.ethan.util.network;

import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClient {

    String URL1 = "http://192.168.43.196/UBasketball/";//手机开热点测试ip
    String URL2 = "http://wyuzww.nat123.net/UBasketball/";//80端口映射ip
    String URL3 = "http://wyuzww.nat123.cc:17205/UBasketball/";//非80端口映射ip
    String URL4 = "http://192.168.1.104/UBasketball/";//校园网ip
    String URL5 = "http://39.108.164.118/UBasketball/";//阿里云ip
    String URL6 = "http://wyuzww.top/UBasketball/";//阿里云域名
    String URL = URL5;


    public void request_Get(String url, Callback callback) {
        //       String url = "wyuzww.nat123.net/UBasketball/login?user_number="+phoneNummber+"&user_password="+password;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(URL + url)
                .cacheControl(new CacheControl.Builder().noCache().noStore().build())
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
                .cacheControl(new CacheControl.Builder().noCache().noStore().build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void requestImage_Post(String url, MultipartBody multipartBody, Callback callback) {

//        MultipartBody multipartBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//             //   .addFormDataPart("user_image",file.getName(), RequestBody.create(MediaType.parse("image/*"),file))
//                .addPart(formBody)
//                .build();
        Request request = new Request.Builder()
                .url(URL + url)
                .post(multipartBody)
                .cacheControl(new CacheControl.Builder().noCache().noStore().build())
                .build();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(100, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public void request_getImage(String url, Callback callback) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
//        Handler mDelivery = new Handler(Looper.getMainLooper());

        Request request = new Request.Builder()
                .url(URL + url)
                .cacheControl(new CacheControl.Builder().noCache().noStore().build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    public String getURL() {
        return URL;
    }

}
