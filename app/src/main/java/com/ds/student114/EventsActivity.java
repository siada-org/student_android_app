package com.ds.student114;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Admin on 26.10.2015.
 */
class MyWebViewClient2 extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }
}
public class EventsActivity extends Activity {

    private WebView mWebView;

    public void onCreate (Bundle savedInstaneState){
        super.onCreate(savedInstaneState);
        setContentView(R.layout.files_layout);

        mWebView = (WebView) findViewById(R.id.files_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient2());

        mWebView.loadUrl("http://rbik.ru.swtest.ru/events_list.php");

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}