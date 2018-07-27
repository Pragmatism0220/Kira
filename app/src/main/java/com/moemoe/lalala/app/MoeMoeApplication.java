package com.moemoe.lalala.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.moemoe.lalala.R;
import com.moemoe.lalala.di.components.DaggerNetComponent;
import com.moemoe.lalala.di.components.NetComponent;
import com.moemoe.lalala.di.modules.NetModule;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.model.entity.StageLineEntity;
import com.moemoe.lalala.model.entity.StageLineOptionsEntity;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.UnCaughtException;
import com.moemoe.lalala.utils.VideoConfig;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.BagOpenActivity;
import com.moemoe.lalala.view.activity.CoinShopActivity;
import com.moemoe.lalala.view.activity.DailyTaskActivity;
import com.moemoe.lalala.view.activity.MapActivity;
import com.moemoe.lalala.view.activity.NewBagV5Activity;
import com.moemoe.lalala.view.activity.NoticeActivity;
import com.moemoe.lalala.view.activity.PersonalV2Activity;
import com.moemoe.lalala.view.activity.PhoneMainV2Activity;
import com.moemoe.lalala.view.activity.PhoneMenuV3Activity;
import com.moemoe.lalala.view.activity.WebViewActivity;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

import static com.moemoe.lalala.view.activity.BaseAppCompatActivity.UUID;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        RongIM.init(this);
        initLogger();
        MoeMoeAppListener.init(this);
//        TCAgent.init(this);
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
//        TCAgent.setReportUncaughtExceptions(true);
        //初始化Leak内存泄露检测工具
        //LeakCanary.install(this);


        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
// 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        CrashReport.initCrashReport(context, strategy);


    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initLogger() {
        LogLevel logLevel;
        if (BuildConfig.DEBUG) {
            logLevel = LogLevel.FULL;
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
//            TCAgent.LOG_ON = false;
        } else {
//            TCAgent.LOG_ON = true;
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
