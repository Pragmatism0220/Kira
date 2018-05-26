package com.moemoe.lalala.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moemoe.lalala.utils.BrowserJsInject;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;

/**
 * Created by Haru on 2016/4/29 0029.
 */
public class CustomWebViewClient extends WebViewClient {
    private BaseAppCompatActivity mActivity;

    public CustomWebViewClient(BaseAppCompatActivity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        ((CustomWebView) view).notifyPageFinished();
        view.loadUrl(BrowserJsInject.fullScreenByJs(url));
        mActivity.onPageFinished(url);
        mActivity.cancelProgressBar();
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        ((CustomWebView) view).notifyPageStarted();
        mActivity.onPageStarted(url);
//        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        handler.proceed();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.getSettings()
//                    .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) return false;


        try{
            if(!url.startsWith("http://") && !url.startsWith("https://")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mActivity.startActivity(intent);
                return true;
            }
        }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
            return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
        }

        // TODO Auto-generated method stub
        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        view.loadUrl(url);
        return true;
        
//        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }
}
