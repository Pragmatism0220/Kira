package com.moemoe.lalala.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.moemoe.lalala.R;
import com.moemoe.lalala.di.components.DaggerNetComponent;
import com.moemoe.lalala.di.components.NetComponent;
import com.moemoe.lalala.di.modules.NetModule;
import com.moemoe.lalala.greendao.gen.DeskmateUserEntilsDao;
import com.moemoe.lalala.model.entity.DeskmateImageEntity;
import com.moemoe.lalala.model.entity.DeskmateUserEntils;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.UnCaughtException;
import com.moemoe.lalala.utils.VideoConfig;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.MapActivity;
import com.moemoe.lalala.view.widget.map.model.MapObject;
import com.moemoe.lalala.view.widget.tooltip.TooltipAnimation;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.net.Proxy;
import java.util.ArrayList;

import io.rong.imkit.RongIM;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
    public ArrayList<Activity> activities = new ArrayList<>();
    private TextView mIvBubble;
    private View inflate;
    private boolean isWindow;
    private int functionalX;
    private int functionalY;
    private DeskmateUserEntils entilsTop;
    private DeskmateUserEntils entilsBottom;
    private DeskmateUserEntils entilsLeft;
    private DeskmateUserEntils entilsRight;
    private DeskmateUserEntils entilsDrag;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        isWindow = true;
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
    }

    public void GoneWindowMager(Context context) {
        if (windowManager != null && inflate != null && wmParams != null) {
//            imageView.setVisibility(View.GONE);
            inflate.setVisibility(View.GONE);
        }
    }

    public void VisibilityWindowMager(Context context) {
        if (windowManager != null && inflate != null && wmParams != null) {
//            imageView.setVisibility(View.VISIBLE);
            inflate.setVisibility(View.VISIBLE);
        }
    }

    public void removeWindowMager() {
        if (windowManager != null && inflate != null) {
            windowManager.removeViewImmediate(inflate);
        }
    }

    public View getInflate() {
        return inflate;
    }

    public boolean isWindow() {
        return isWindow;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    public void initWindowManager(final Context context, WindowManager mWindowManager) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                return;
            }
        }
        if (!isWindow) {
            return;
        } else {
            isWindow = false;
        }
        windowManager = mWindowManager;
        wmParams = new WindowManager.LayoutParams();
        //重点，类型设置为dialog类型,可无视权限!
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;

//        //重点,必须设置此参数，用于窗口机制验证
//        if (context instanceof FeedV3Activity) {
//            IBinder windowToken = ((FeedV3Activity) context).getWindow().getDecorView().getWindowToken();
//            wmParams.token = windowToken;
//        } else if (context instanceof MapActivity) {
//            IBinder windowToken = ((MapActivity) context).getWindow().getDecorView().getWindowToken();
//            wmParams.token = windowToken;
//
//        }
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = width;
        wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x110);
        functionalX = width;
        functionalY = height / 2 - (int) getResources().getDimension(R.dimen.x110);
        inflate = LayoutInflater.from(context).inflate(R.layout.ac_window, null);
        mIvBubble = inflate.findViewById(R.id.tv_bottom_left);
        imageView = inflate.findViewById(R.id.imageView);
        imageView.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.tv_bottom_left);
        if (getActivity(MapActivity.class.getName()) instanceof MapActivity) {
            windowManager.addView(inflate, wmParams);
        }
        DeskmateUserEntils entity = entilsLeft;
        Drawable drawable;
        if (!TextUtils.isEmpty(entity.getPath())) {
            drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
        }
        if (drawable != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (int) entity.getW();
            params.height = (int) entity.getH();
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(drawable);
        } else {
            FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
        }
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
                            if (getActivity(MapActivity.class.getName()) instanceof MapActivity && isActivityTop(MapActivity.class, context)) {
                                ((MapActivity) getActivity(MapActivity.class.getName())).clickSelect();
                            }
                            DeskmateUserEntils entity = entilsDrag;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_drop);
                            }
                            if (drawable != null) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                params.width = (int) entity.getW();
                                params.height = (int) entity.getH();
                                imageView.setLayoutParams(params);
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                            }
                        }

                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        wmParams.x = curx - inflate.getMeasuredWidth() / 2;
                        // 减25为状态栏的高度
                        wmParams.y = cury - inflate.getMeasuredWidth() / 2;
                        // 刷新
                        windowManager.updateViewLayout(inflate, wmParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int finalX = (int) event.getRawX();
                        int finalY = (int) event.getRawY();
                        boolean isok = false;

                        if (finalY < inflate.getMeasuredHeight()) {
                            movey = 0;
                            movex = finalX - inflate.getMeasuredWidth() / 2;
                            DeskmateUserEntils entity = entilsTop;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_top);
                            }
                            if (drawable != null) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                params.width = (int) entity.getW();
                                params.height = (int) entity.getH();
                                imageView.setLayoutParams(params);
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                            }
                        }


                        if (finalY > height - inflate.getMeasuredHeight()) {
                            movey = height;
                            movex = finalX - inflate.getMeasuredWidth() / 2;
                            DeskmateUserEntils entity = entilsBottom;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_down);
                            }
                            if (drawable != null) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                params.width = (int) entity.getW();
                                params.height = (int) entity.getH();
                                imageView.setLayoutParams(params);
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                            }
                        }

                        if (finalY > inflate.getMeasuredHeight() && finalY < height - inflate.getMeasuredHeight()) {
                            isok = true;
                        }
                        if (isok && finalX - inflate.getMeasuredWidth() / 2 < width / 2) {
                            movex = 0;
                            movey = finalY - inflate.getMeasuredHeight() / 2;
                            DeskmateUserEntils entity = entilsLeft;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_left);
                            }
                            if (drawable != null) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                params.width = (int) entity.getW();
                                params.height = (int) entity.getH();
                                imageView.setLayoutParams(params);
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                            }
                        } else if (isok && finalX - inflate.getMeasuredWidth() / 2 > width / 2) {
                            movex = width;
                            movey = finalY - inflate.getMeasuredHeight() / 2;
                            DeskmateUserEntils entity = entilsRight;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getMapRootPath() + entity.getPath());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
                            }
                            if (drawable != null) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                params.width = (int) entity.getW();
                                params.height = (int) entity.getH();
                                imageView.setLayoutParams(params);
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                            }
                        }

                        wmParams.x = movex;
                        wmParams.y = movey;
                        if (isMove) {
                            functionalX = movex;
                            functionalY = movey;
                            windowManager.updateViewLayout(inflate, wmParams);
                        }
                        return isMove;//false 为点击 true 为移动
                    default:
                        break;
                }
                return false;
            }
        });
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity(MapActivity.class.getName()) instanceof MapActivity && isActivityTop(MapActivity.class, context)) {
                        ((MapActivity) getActivity(MapActivity.class.getName())).windowManagerOnclick(functionalX, functionalY, inflate.getMeasuredWidth(), inflate.getMeasuredHeight());
                    }
                }
            });
        }
    }

    /**
     * 数据压制
     */
    public void goGreenDao() {
        ArrayList<DeskmateUserEntils> entilsDao = (ArrayList<DeskmateUserEntils>) GreenDaoManager.getInstance().getSession().getDeskmateUserEntilsDao()
                .queryBuilder()
                .where(DeskmateUserEntilsDao.Properties.Id.eq("deskmate"))
                .list();
        if (entilsDao != null && entilsDao.size() > 0) {
            for (DeskmateUserEntils entils : entilsDao) {
                if (entils.getRemark().equals("top")) {
                    entilsTop = entils;
                } else if (entils.getRemark().equals("bottom")) {
                    entilsBottom = entils;
                } else if (entils.getRemark().equals("left")) {
                    entilsLeft = entils;
                } else if (entils.getRemark().equals("right")) {
                    entilsRight = entils;
                } else if (entils.getRemark().equals("drag")) {
                    entilsDrag = entils;
                }
            }
        }
    }

    /**
     * 判断某activity是否处于栈顶
     *
     * @return true在栈顶 false不在栈顶
     */

    private boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    public static Activity getActivity(String className) {
        for (Activity activity : getInstance().activities) {
            if (activity.getClass().getName().equals(className)) {
                return activity;
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
