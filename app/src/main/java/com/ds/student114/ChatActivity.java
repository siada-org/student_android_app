package com.ds.student114;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClients extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }
}
public class ChatActivity extends Activity {

    private WebView mWebView;

    public void onCreate (Bundle savedInstaneState){
        super.onCreate(savedInstaneState);
        setContentView(R.layout.files_layout);

        mWebView = (WebView) findViewById(R.id.files_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClients());

        mWebView.loadUrl("http://rbik.ru.swtest.ru/cometchat/extensions/mobilewebapp/#lobby");

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