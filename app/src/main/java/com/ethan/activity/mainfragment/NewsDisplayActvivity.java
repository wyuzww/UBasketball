package com.ethan.activity.mainfragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ethan.R;
import com.ethan.util.Utils;

public class NewsDisplayActvivity extends AppCompatActivity implements View.OnClickListener {
    private Button back_BT;

    private String newsUrl;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);

        new Utils().setFullScreen(getWindow());
        bindView();
        getWeb();
        deleteCache();

    }

    private void getWeb() {
        newsUrl = getIntent().getStringExtra("news_url");
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient());
        webView.clearCache(true);
        webView.clearFormData();
        webView.loadUrl(newsUrl);
    }

    private void bindView() {
        back_BT = findViewById(R.id.back_button);

        back_BT.setOnClickListener(this);
    }

    private void deleteCache() {
        if (webView != null) {
            webView.clearCache(true);
            webView.clearFormData();
            getCacheDir().delete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }
}
