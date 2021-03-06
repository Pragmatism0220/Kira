package com.moemoe.lalala.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.AppStatusConstant;
import com.moemoe.lalala.app.AppStatusManager;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.schedulers.Schedulers;

/**
 * base activity
 * Created by yi on 2016/11/27.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected ProgressDialog mDialog;
    public static final String UUID = "uuid";
    public ProgressBar mProgressBar;
    private Unbinder bind;
    private int mStayTime;
    private WindowManager.LayoutParams wmParams;
    private ImageView imageView;
    private int width;
    private int height;
    private Handler mHandler = new Handler();
    private Runnable timeRunnabel = new Runnable() {
        @Override
        public void run() {
            mStayTime++;
            mHandler.postDelayed(this, 1000);
        }
    };

    protected void startTime() {
        mHandler.post(timeRunnabel);
    }

    protected void pauseTime() {
        mHandler.removeCallbacks(timeRunnabel);
    }

    /**
     * 点击事件标记
     *
     * @param event 事件名 {"doc":""} {"dynamic":""}
     */
    protected void clickEvent(String event) {
        MoeMoeApplication.getInstance().getNetComponent().getApiService().clickDepartment(event)
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
    }

    /**
     * 页面进入的时间计算
     *
     * @param event 事件名
     */
    protected void stayEvent(String event) {
        MoeMoeApplication.getInstance().getNetComponent().getApiService().stayDepartment(event, mStayTime)
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);

        this.getWindow().setBackgroundDrawable(null);//移除默认背景，避免overdraw

        if (this instanceof PhoneAlarmActivity) {
            this.setContentView(this.getLayoutId());
            bind = ButterKnife.bind(this);
            this.initViews(savedInstanceState);
            this.initToolbar(savedInstanceState);
            this.initData();
            this.initListeners();
        } else {
            switch (AppStatusManager.getInstance().getAppStatus()) {
                case AppStatusConstant.STATUS_FORCE_KILLED:
                    restartApp();
                    break;
                case AppStatusConstant.STATUS_NORMAL:
                    this.setContentView(this.getLayoutId());
                    bind = ButterKnife.bind(this);
                    this.initViews(savedInstanceState);
                    this.initToolbar(savedInstanceState);
                    this.initData();
                    this.initListeners();
                    break;
            }
        }
    }

    protected void restartApp() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
    }

    /**
     * Fill int layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * Initialize the toolbar in the layout
     *
     * @param savedInstanceState
     */
    protected abstract void initToolbar(Bundle savedInstanceState);

    /**
     * Initalize the view Of the listener
     */
    protected abstract void initListeners();

    /**
     * Initialize the Activity Data
     */
    protected abstract void initData();

    public void showToast(String msg) {
        ToastUtils.showShortToast(this, msg);
    }

    public void showToast(int resId) {
        ToastUtils.showShortToast(this, resId);
    }

    public void createDialog(String message) {
        if (this.isFinishing())
            return;
        try {
            if (mDialog != null)
                mDialog.dismiss();
            mDialog = ProgressDialog.show(this, "", message);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void createDialog() {
        if (this.isFinishing())
            return;
        try {
            if (mDialog != null && mDialog.isShowing()) return;
            mDialog = ProgressDialog.show(this, "", getString(R.string.msg_on_loading));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void finalizeDialog() {
        if (mDialog == null) return;
        try {
            if (mDialog.isShowing()) mDialog.dismiss();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        mDialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
        if (AppSetting.OpenUmeng) {
            MobclickAgent.onResume(this);
//            TCAgent.onPageStart(this, this.getClass().getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
        if (AppSetting.OpenUmeng) {
            MobclickAgent.onPause(this);
//            TCAgent.onPageEnd(this, this.getClass().getName());
        }
    }

    /**
     * 网页加载完成执行
     */
    public void onPageFinished(String url) {

    }

    /**
     * 关闭网页进度条
     */
    public void cancelProgressBar() {

    }

    /**
     * 网页加载开始
     */
    public void onPageStarted(String url) {
    }

    public void showCustomView(View view, WebChromeClient.CustomViewCallback callback, boolean isUseNew) {

    }

    public void hideCustomView(boolean isUseNew) {
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V $(@IdRes int id) {
        return (V) findViewById(id);
    }
}
