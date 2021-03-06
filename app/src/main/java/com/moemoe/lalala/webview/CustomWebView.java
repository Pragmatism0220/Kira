package com.moemoe.lalala.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.moemoe.lalala.event.BackSchoolEvent;
import com.moemoe.lalala.model.entity.MotaResult;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.WebViewActivity;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Haru on 2016/4/29 0029.
 */
public class CustomWebView extends WebView {

    private static boolean mBoMethodsLoaded = false;
    private static Method mOnPauseMethod = null;
    private static Method mOnResumeMethod = null;
    private static Method mSetFindIsUp = null;
    private static Method mNotifyFindDialogDismissed = null;

    private Context mContext;
    private BaseAppCompatActivity mActivity;
    private int mProgress = 100;
    private boolean mIsLoading = false;
    private String mLoadedUrl;
    private CustomWebChromeClient mChromeClient;
    private boolean addedJavascriptInterface;

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd() { // Must match Javascript interface method of VideoEnabledWebChromeClient
            // This code is not executed in the UI thread, so we must force that to happen
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mChromeClient != null) {
                        mChromeClient.onHideCustomView();
                    }
                }
            });
        }
    }

    public class JavascriptFull {
        @android.webkit.JavascriptInterface
        public void playing() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mChromeClient.isVideoFullscreen()) {
                        mChromeClient.isVideoFullscreen = false;
                        ((BaseAppCompatActivity) mContext).hideCustomView(false);
                    } else {
                        ((BaseAppCompatActivity) mContext).showCustomView(null, null, false);
                        mChromeClient.isVideoFullscreen = true;
                    }
                }
            });
        }
    }


    public class InJavaScriptLocalObj {
        @android.webkit.JavascriptInterface
        public void neta_chapter(int pass) {
            //ToastUtil.showCenterToast(mContext,"通过了:" + pass, Toast.LENGTH_LONG);
            PreferenceUtils.savePassEvent(mActivity, pass);
            if (pass == 5) {
                ((WebViewActivity) mActivity).saveLive2d();
            }
            mActivity.finish();
        }
    }

    public class NetaJs {
        @android.webkit.JavascriptInterface
        public void neta_pay(String data) {
//            if(data.contains("qpay")){
//                Pingpp.createPayment(mActivity, data,"qwallet1104765197");
//            }else {
//                Pingpp.createPayment(mActivity, data);
//            }
            PingppUI.createPay(mActivity, data, new PaymentHandler() {
                @Override
                public void handlePaymentResult(Intent intent) {
                    String result = intent.getExtras().getString("result");
                    if (result.contains("success")) {
                        result = "success";
                    } else if (result.contains("fail")) {
                        result = "fail";
                    } else if (result.contains("cancel")) {
                        result = "cancel";
                    } else if (result.contains("invalid")) {
                        result = "invalid";
                    }
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
//                    String errorMsg = intent.getExtras().getString("error_msg"); // 错误信息
//                    String extraMsg = intent.getExtras().getString("extra_msg"); // 错误信息
                    // showMsg(result, errorMsg, extraMsg);
                    MotaResult pay = new MotaResult();
                    pay.setResult(result);
                    pay.setType("pay");
                    Gson gson = new Gson();
                    String temp = gson.toJson(pay);
                    ((WebViewActivity) mActivity).resultMota(temp);
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void neta_share() {
            ((WebViewActivity) mActivity).showShare(1);
        }

        @android.webkit.JavascriptInterface
        public void neta_share_v2(int type) {
            ((WebViewActivity) mActivity).showShare(type);
        }

        @android.webkit.JavascriptInterface
        public void neta_back() {
            ((WebViewActivity) mActivity).setmIvBack();
        }

        @android.webkit.JavascriptInterface
        public void neta_share_v3(String num, String per, String lines) {
            ((WebViewActivity) mActivity).showShare(num, per, lines);
        }

        @android.webkit.JavascriptInterface
        public void neta_back_school(int pass) {
            int i = PreferenceUtils.getBackSchoolLevel(mActivity);
            if (pass > i) {
                PreferenceUtils.setBackSchoolLevel(mActivity, pass);
                EventBus.getDefault().post(new BackSchoolEvent(pass));
            }
            mActivity.finish();
        }
    }

    public CustomWebView(Context context) {
        super(context);
        mContext = context;
        addedJavascriptInterface = false;
        initializeOptions();
        loadMethods();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        addedJavascriptInterface = false;
        initializeOptions();
        loadMethods();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addedJavascriptInterface = false;
    }

    public boolean isVideoFullscreen() {
        return mChromeClient != null && mChromeClient.isVideoFullscreen();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setWebChromeClient(WebChromeClient client, BaseAppCompatActivity activity) {
        if (client instanceof CustomWebChromeClient) {
            this.mChromeClient = (CustomWebChromeClient) client;
        }
        mActivity = activity;
        this.setWebChromeClient(client);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private void addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            // Add javascript interface to be called when the video ends (must be done before page load)
            addJavascriptInterface(new JavascriptInterface(), "_VideoEnabledWebView"); // Must match Javascript interface name of VideoEnabledWebChromeClient
            addJavascriptInterface(new JavascriptFull(), "local_obj");
            addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj1");
            addJavascriptInterface(new NetaJs(), "neta");
            addedJavascriptInterface = true;
        }
    }

    @Override
    public boolean canGoForward() {
        return super.canGoForward();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    public void initializeOptions() {
        WebSettings settings = getSettings();
        settings.setLoadsImagesAutomatically(true);//可以加载图片
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        settings.setUserAgentString("");
        requestFocus();
        settings.setUseWideViewPort(true);//能缩放
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setSupportZoom(true);
        settings.setSupportMultipleWindows(true);
        setLongClickable(true);
        setScrollbarFadingEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setDrawingCacheEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
    }

    @Override
    public void loadUrl(String url) {
        mLoadedUrl = url;
        addJavascriptInterface();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        mLoadedUrl = url;
        addJavascriptInterface();
        super.loadUrl(url, additionalHttpHeaders);
    }

    /**
     * Set the current loading progress of this view.
     *
     * @param progress The current loading progress.
     */
    public void setProgress(int progress) {
        mProgress = progress;
    }

    /**
     * Get the current loading progress of the view.
     *
     * @return The current loading progress of the view.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Triggered when a new page loading is requested.
     */
    public void notifyPageStarted() {
        mIsLoading = true;
    }

    /**
     * Triggered when the page has finished loading.
     */
    public void notifyPageFinished() {
        mProgress = 100;
        mIsLoading = false;
    }

    /**
     * Check if the view is currently loading.
     *
     * @return True if the view is currently loading.
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * Get the loaded url, e.g. the one asked by the user, without redirections.
     *
     * @return The loaded url.
     */
    public String getLoadedUrl() {
        return mLoadedUrl;
    }

    /**
     * Reset the loaded url.
     */
    public void resetLoadedUrl() {
        mLoadedUrl = null;
    }

    public boolean isSameUrl(String url) {
        if (url != null) {
            return url.equalsIgnoreCase(this.getUrl());
        }

        return false;
    }

    /**
     * Perform an 'onPause' on this WebView through reflexion.
     */
    public void doOnPause() {
        if (mOnPauseMethod != null) {
            try {

                mOnPauseMethod.invoke(this);

            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    /**
     * Perform an 'onResume' on this WebView through reflexion.
     */
    public void doOnResume() {
        if (mOnResumeMethod != null) {
            try {

                mOnResumeMethod.invoke(this);

            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    public void doSetFindIsUp(boolean value) {
        if (mSetFindIsUp != null) {
            try {

                mSetFindIsUp.invoke(this, value);

            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    public void doNotifyFindDialogDismissed() {
        if (mNotifyFindDialogDismissed != null) {
            try {

                mNotifyFindDialogDismissed.invoke(this);

            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    /**
     * Load static reflected methods.
     */
    private void loadMethods() {

        if (!mBoMethodsLoaded) {
            try {
                mOnPauseMethod = WebView.class.getMethod("onPause");
                mOnResumeMethod = WebView.class.getMethod("onResume");
            } catch (SecurityException e) {
                mOnPauseMethod = null;
                mOnResumeMethod = null;
            } catch (NoSuchMethodException e) {
                mOnPauseMethod = null;
                mOnResumeMethod = null;
            }
            try {
                mSetFindIsUp = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                mNotifyFindDialogDismissed = WebView.class.getMethod("notifyFindDialogDismissed");
            } catch (SecurityException e) {
                mSetFindIsUp = null;
                mNotifyFindDialogDismissed = null;
            } catch (NoSuchMethodException e) {
                mSetFindIsUp = null;
                mNotifyFindDialogDismissed = null;
            }
            mBoMethodsLoaded = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

}
