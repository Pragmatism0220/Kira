package com.moemoe.lalala.app;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.moemoe.lalala.R;
import com.moemoe.lalala.di.components.DaggerNetComponent;
import com.moemoe.lalala.di.components.NetComponent;
import com.moemoe.lalala.di.modules.NetModule;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.UnCaughtException;
import com.moemoe.lalala.utils.VideoConfig;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.MapActivity;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.net.Proxy;

import io.rong.imkit.RongIM;

/**
 * 应用类
 * Created by yi on 2017/5/11.
 */

public class MoeMoeApplication extends Application {
    /**
     * 当前进程允许的最大内存
     */
    public static long PROCESS_MEMORY_LIMIT;
    private static MoeMoeApplication mInstance = null;
    private NetComponent netComponent;
    private WindowManager.LayoutParams wmParams;
    private ImageView imageView;
    private int width;
    private int height;
    private WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        RongIM.init(this);
        initLogger();
        MoeMoeAppListener.init(this);
        initNet();
        GreenDaoManager.getInstance();
        UnCaughtException caughtException = UnCaughtException.getInstance();
        TagControl.getInstance().init(this);
        caughtException.init(this);
        VideoConfig.init(this);
        MusicPreferences.init(this);
//        //初始化AD
//        AdHub.initialize(this,"1756");
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit();
        //初始化Leak内存泄露检测工具
        //LeakCanary.install(this);
    }

    public void GoneWindowMager() {
        if (windowManager != null && imageView != null && wmParams != null) {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    public void VisibilityWindowMager() {
        if (windowManager != null && imageView != null && wmParams != null) {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void removeWindowMager() {

    }

    public void initWindowManager(WindowManager mWindowManager) {
        windowManager = mWindowManager;
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = width / 2 - (int) getResources().getDimension(R.dimen.x110);
        wmParams.y = height;
//        wmParams.x = 0;
//        wmParams.y = 0;
        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.btn_classmate_len_down);
        imageView.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        windowManager.addView(imageView, wmParams);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            int lastx = 0;
            int lasty = 0;
            int movex = 0;
            int movey = 0;
            boolean isMove;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastx = (int) event.getRawX();
                        lasty = (int) event.getRawY();
                        isMove = false;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        int curx = (int) event.getRawX();
                        int cury = (int) event.getRawY();


                        int x;
                        int y;
                        x = Math.abs(curx - lastx);
                        y = Math.abs(cury - lasty);
                        if (x < 5 || y < 5) {
                            isMove = false;
                            return false;
                        } else {
                            isMove = true;
                            imageView.setImageResource(R.drawable.btn_classmate_len_drop);
                        }

                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        wmParams.x = curx - imageView.getMeasuredWidth() / 2;
                        // 减25为状态栏的高度
                        wmParams.y = cury - imageView.getMeasuredWidth() / 2;
                        // 刷新
                        windowManager.updateViewLayout(imageView, wmParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int finalX = (int) event.getRawX();
                        int finalY = (int) event.getRawY();
                        boolean isok = false;

                        if (finalY < imageView.getMeasuredHeight()) {
                            movey = 0;
                            movex = finalX - imageView.getMeasuredWidth() / 2;
                            imageView.setImageResource(R.drawable.btn_classmate_len_top);
                        }


                        if (finalY > height - imageView.getMeasuredHeight()) {
                            movey = height - imageView.getMeasuredHeight();
                            movex = finalX - imageView.getMeasuredWidth() / 2;
                            imageView.setImageResource(R.drawable.btn_classmate_len_down);
                        }


                        if (finalY > imageView.getMeasuredHeight() && finalY < height - imageView.getMeasuredHeight()) {
                            isok = true;
                        }
                        if (isok && finalX - imageView.getMeasuredWidth() / 2 < width / 2) {
                            movex = 0;
                            movey = finalY - imageView.getMeasuredHeight() / 2;
                            imageView.setImageResource(R.drawable.btn_classmate_len_left);
                        } else if (isok && finalX - imageView.getMeasuredWidth() / 2 > width / 2) {
                            movex = width - imageView.getMeasuredWidth();
                            movey = finalY - imageView.getMeasuredHeight() / 2;
                            imageView.setImageResource(R.drawable.btn_classmate_len_right);
                        }

                        wmParams.x = movex;
                        wmParams.y = movey;
                        if (isMove) {
                            windowManager.updateViewLayout(imageView, wmParams);
                        }
                        return isMove;//false 为点击 true 为移动
                    default:
                        break;
                }
                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if ((MapActivity) getApplicationContext() instanceof MapActivity) {
//                    ((MapActivity) getApplicationContext()).windowManagerOnclick();
//                }

            }
        });

    }

    private void initLogger() {
        LogLevel logLevel;
        if (BuildConfig.DEBUG) {
            logLevel = LogLevel.FULL;
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        } else {
            logLevel = LogLevel.NONE;
            QueryBuilder.LOG_SQL = false;
            QueryBuilder.LOG_VALUES = false;
        }
        Logger.init("NetaApp")
                .methodCount(3)
                .logLevel(logLevel);
    }

    private void initNet() {
        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(this))
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static MoeMoeApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MoeMoeApplication();
        }
        return mInstance;
    }
}
