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

import org.greenrobot.greendao.query.QueryBuilder;

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
    private WindowManager.LayoutParams wmParams;
    private ImageView imageView;
    private int width;
    private int height;
    private WindowManager windowManager;
    public ArrayList<Activity> activities = new ArrayList<>();
    private View inflate;
    private boolean isWindow;
    private int functionalX;
    private int functionalY;
    private DeskmateEntils entilsTop;
    private DeskmateEntils entilsBottom;
    private DeskmateEntils entilsLeft;
    private DeskmateEntils entilsRight;
    private DeskmateEntils entilsDrag;
    private WindowManager.LayoutParams wmParamsTwo;
    private View inflateTwo;
    private RelativeLayout mRlRenWu;
    private RelativeLayout mLlFrist;
    private ImageView mPersonal, mBag, mShopping, mSignRoot, mPhoneMenu, mIvMsg;
    private boolean isMove;
    private WindowManager.LayoutParams wmParamsThree;
    private View dialog;
    private TextView mTvContent;
    private StageLineEntity stageLineEntity;
    private TextView mIvLeft;
    private TextView mIvCansl;
    private LinearLayout mRlSelect;
    private RelativeLayout mRlDialog;

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
            imageView.setVisibility(View.GONE);
            inflate.setVisibility(View.GONE);
        }
    }

    public void GoneMenu() {
        if (windowManager != null && inflateTwo != null && inflateTwo.getVisibility() == View.VISIBLE) {
            inflateTwo.setVisibility(View.GONE);
        }
    }

    public boolean isMenu() {
        if (windowManager != null && inflateTwo != null && inflateTwo.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void VisibilityWindowMager(Context context) {
        if (windowManager != null && inflate != null && wmParams != null) {
            imageView.setVisibility(View.VISIBLE);
            inflate.setVisibility(View.VISIBLE);
        }
    }

    public boolean GoneDiaLog() {
        if (windowManager != null && dialog != null && dialog.getVisibility() == View.VISIBLE) {
            dialog.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public void removeWindowMager() {
        if (windowManager != null && inflate != null && inflateTwo != null && dialog != null) {
            windowManager.removeViewImmediate(inflate);
            windowManager.removeViewImmediate(inflateTwo);
            windowManager.removeViewImmediate(dialog);
        }
    }

    public View getInflate() {
        return inflate;
    }

    public boolean isWindow() {
        return isWindow;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    public void initWindowManager(final Context context, final WindowManager mWindowManager) {
        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this)) {
//                return;
//            }
        }
        if (!isWindow) {
            return;
        } else {
            isWindow = false;
        }
        windowManager = mWindowManager;
        wmParams = new WindowManager.LayoutParams();
        //重点，类型设置为dialog类型,可无视权限!

        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        width = windowManager.getDefaultDisplay().getWidth();
        height = getResources().getDisplayMetrics().heightPixels;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置图片格式，效果为背景透明  
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        final DeskmateEntils entity = entilsRight;
//        wmParams.x = width - (int) getResources().getDimension(R.dimen.x280);
//        wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x180);
        wmParams.x = width - (int) getResources().getDimension(R.dimen.x280);
        wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x180);
        inflate = LayoutInflater.from(context).inflate(R.layout.ac_window, null);
        imageView = inflate.findViewById(R.id.imageView);
        final Drawable drawable;
        if (!TextUtils.isEmpty(entity.getPath())) {
            drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
        }
        functionalX = width - (int) getResources().getDimension(R.dimen.x280);
        functionalY = height / 2 - (int) getResources().getDimension(R.dimen.x180);
        if (getActivity(MapActivity.class.getName()) instanceof MapActivity) {
            windowManager.addView(this.inflate, wmParams);
        }


        wmParamsTwo = new WindowManager.LayoutParams();
        //重点，类型设置为dialog类型,可无视权限!
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            wmParamsTwo.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParamsTwo.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParamsTwo.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParamsTwo.width = (int) context.getResources().getDimension(R.dimen.x428);
        wmParamsTwo.height = (int) context.getResources().getDimension(R.dimen.y320);

        //设置图片格式，效果为背景透明  
        wmParamsTwo.format = PixelFormat.RGBA_8888;
        wmParamsTwo.gravity = Gravity.LEFT | Gravity.TOP;
        wmParamsTwo.x = 0;
        wmParamsTwo.y = 0;
        inflateTwo = LayoutInflater.from(context).inflate(R.layout.float_renwu_layout, null);
        inflateTwo.setVisibility(View.GONE);
        mRlRenWu = inflateTwo.findViewById(R.id.rl_renwu);
        mLlFrist = inflateTwo.findViewById(R.id.ll_frist);
        mPersonal = inflateTwo.findViewById(R.id.iv_personal);
        mBag = inflateTwo.findViewById(R.id.iv_bag);
        mShopping = inflateTwo.findViewById(R.id.iv_shopping);
        mSignRoot = inflateTwo.findViewById(R.id.iv_sign_root);
        mPhoneMenu = inflateTwo.findViewById(R.id.iv_phone_menu);
        mIvMsg = inflateTwo.findViewById(R.id.iv_msg);
        if (getActivity(MapActivity.class.getName()) instanceof MapActivity) {
            windowManager.addView(inflateTwo, wmParamsTwo);
        }


        wmParamsThree = new WindowManager.LayoutParams();
        //重点，类型设置为dialog类型,可无视权限!
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            wmParamsThree.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParamsThree.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParamsThree.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParamsThree.width = (int) context.getResources().getDimension(R.dimen.x428);
        wmParamsThree.height = (int) context.getResources().getDimension(R.dimen.y320);

        //设置图片格式，效果为背景透明  
        wmParamsThree.format = PixelFormat.RGBA_8888;
        wmParamsThree.gravity = Gravity.LEFT | Gravity.TOP;
        wmParamsThree.x = 0;
        wmParamsThree.y = 0;
        dialog = LayoutInflater.from(context).inflate(R.layout.float_dialog_layout, null);
        dialog.setVisibility(View.GONE);
        mTvContent = dialog.findViewById(R.id.tv_content);
        mRlDialog = dialog.findViewById(R.id.rl_dialog);
        mIvLeft = dialog.findViewById(R.id.iv_left);
        mIvCansl = dialog.findViewById(R.id.iv_right);
        mRlSelect = dialog.findViewById(R.id.rl_select);
        final String stageLine = PreferenceUtils.getStageLine(context);
        Gson gson = new Gson();
        stageLineEntity = gson.fromJson(stageLine, StageLineEntity.class);
        if (getActivity(MapActivity.class.getName()) instanceof MapActivity) {
            windowManager.addView(dialog, wmParamsThree);
        }

        imageView.setOnTouchListener(new View.OnTouchListener() {
            int lastx = 0;
            int lasty = 0;
            int movex = 0;
            int movey = 0;

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
                            inflateTwo.setVisibility(View.GONE);
                            dialog.setVisibility(View.GONE);
                            DeskmateEntils entity = entilsDrag;
                            Drawable drawable;
                            if (!TextUtils.isEmpty(entity.getPath())) {
                                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                            } else {
                                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_drop);
                            }
                            if (drawable != null) {
                                imageView.setImageDrawable(drawable);
                            } else {
                                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
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

                        if (finalX > width / 2) {
                            if (finalY > height / 2) {
                                if (width - finalX > height - finalY) {
                                    //bootem
                                    DeskmateEntils entity = entilsBottom;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_down);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = finalX;
                                    movey = height - imageView.getMeasuredHeight();
                                } else {
                                    //right
                                    DeskmateEntils entity = entilsRight;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = width - imageView.getMeasuredWidth();
                                    movey = finalY;
                                }
                            } else {
                                if (width - finalX > finalY) {
                                    //top
                                    DeskmateEntils entity = entilsTop;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_top);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = finalX;
                                    movey = 0;
                                } else {
                                    //right
                                    DeskmateEntils entity = entilsRight;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = width - imageView.getMeasuredWidth();
                                    movey = finalY;
                                }
                            }
                        } else {
                            if (finalY > height / 2) {
                                if (finalX > height - finalY) {
                                    //bottem
                                    DeskmateEntils entity = entilsBottom;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_down);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = finalX;
                                    movey = height - imageView.getMeasuredHeight();
                                } else {
                                    //left
                                    DeskmateEntils entity = entilsLeft;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_left);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = 0;
                                    movey = finalY;
                                }
                            } else {
                                if (finalX > finalY) {
                                    //top 
                                    DeskmateEntils entity = entilsTop;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_top);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = finalX;
                                    movey = 0;
                                } else {
                                    //left
                                    DeskmateEntils entity = entilsLeft;
                                    Drawable drawable;
                                    if (!TextUtils.isEmpty(entity.getPath())) {
                                        drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    } else {
                                        drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_left);
                                    }
                                    if (drawable != null) {
                                        imageView.setImageDrawable(drawable);
                                    } else {
                                        FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                                    }
                                    movex = 0;
                                    movey = finalY;
                                }
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.getVisibility() == View.VISIBLE) {
                    dialog.setVisibility(View.GONE);
                    return;
                }
                if (!isMove) {
                    windowManagerOnclick(functionalX, functionalY, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                }
            }
        });
        mPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.checkNetworkAndShowError(getActivity(MapActivity.class.getName())) && DialogUtils.checkLoginAndShowDlg(getActivity(MapActivity.class.getName()))) {
                    //埋点统计：手机个人中心
                    Intent i1 = new Intent(getActivity(MapActivity.class.getName()), PersonalV2Activity.class);
                    i1.putExtra(UUID, PreferenceUtils.getUUid());
                    getActivity(MapActivity.class.getName()).startActivity(i1);
                }
                inflateTwo.setVisibility(View.GONE);
            }
        });
        mBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.checkNetworkAndShowError(getActivity(MapActivity.class.getName())) && DialogUtils.checkLoginAndShowDlg(getActivity(MapActivity.class.getName()))) {
                    if (PreferenceUtils.getAuthorInfo().isOpenBag()) {
                        Intent i4 = new Intent(getActivity(MapActivity.class.getName()), NewBagV5Activity.class);
                        i4.putExtra("uuid", PreferenceUtils.getUUid());
                        getActivity(MapActivity.class.getName()).startActivity(i4);
                    } else {
                        Intent i4 = new Intent(getActivity(MapActivity.class.getName()), BagOpenActivity.class);
                        getActivity(MapActivity.class.getName()).startActivity(i4);
                    }
                }
                inflateTwo.setVisibility(View.GONE);
            }
        });
        mShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(getActivity(MapActivity.class.getName()), CoinShopActivity.class);
                getActivity(MapActivity.class.getName()).startActivity(i7);
                inflateTwo.setVisibility(View.GONE);

            }
        });
        mSignRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DialogUtils.checkLoginAndShowDlg(getActivity(MapActivity.class.getName()))) {
                    DailyTaskActivity.startActivity(getActivity(MapActivity.class.getName()));
                }
                inflateTwo.setVisibility(View.GONE);
            }
        });
        mPhoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.checkNetworkAndShowError(getActivity(MapActivity.class.getName())) && DialogUtils.checkLoginAndShowDlg(getActivity(MapActivity.class.getName()))) {
                    //埋点统计：通讯录
                    getActivity(MapActivity.class.getName()).startActivity(new Intent(getActivity(MapActivity.class.getName()), PhoneMenuV3Activity.class));
                }
                inflateTwo.setVisibility(View.GONE);
            }
        });
        mIvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.checkNetworkAndShowError(getActivity(MapActivity.class.getName())) && DialogUtils.checkLoginAndShowDlg(getActivity(MapActivity.class.getName()))) {
                    //埋点统计：手机聊天
                    NoticeActivity.startActivity(getActivity(MapActivity.class.getName()), 1);
                }
                inflateTwo.setVisibility(View.GONE);
            }
        });

        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && stageLineEntity != null) {
                    stageLineEntity = getStageLineEntity(stageLineEntity, mIvLeft.getText().toString());
                    if (stageLineEntity == null) {
                        dialog.setVisibility(View.GONE);
                    } else {
                        setDialogView(context, stageLineEntity, dialog);
                    }
                }
            }
        });
        mIvCansl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && stageLineEntity != null) {
                    stageLineEntity = getStageLineEntity(stageLineEntity, mIvCansl.getText().toString());
                    if (stageLineEntity == null) {
                        dialog.setVisibility(View.GONE);
                    } else {
                        setDialogView(context, stageLineEntity, dialog);
                    }
                }
            }
        });

    }

    public void setDialogView(Context context, StageLineEntity entity, View v) {
        if (dialog != null) {
            if (entity.getId() != null) {
                if (!TextUtils.isEmpty(entity.getSchema())) {
                    String temp = entity.getSchema();
                    if (temp.contains("map_event_1.0") || temp.contains("game_1.0")) {
                        if (!DialogUtils.checkLoginAndShowDlg(context)) {
                            return;
                        }
                    }
                    if (temp.contains("http://s.moemoe.la/game/devil/index.html")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
                    }

                    if (temp.contains("http://kiratetris.leanapp.cn/tab001/index.html")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?id=" + authorInfo.getUserId() + "&name=" + authorInfo.getUserName();
                    }
                    if (temp.contains("http://prize.moemoe.la:8000/mt")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?user_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName();
                    }
                    if (temp.contains("http://prize.moemoe.la:8000/netaopera/chap")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?pass=" + PreferenceUtils.getPassEvent(context) + "&user_id=" + authorInfo.getUserId();
                    }
                    if (temp.contains("http://neta.facehub.me/")) {
                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                        temp += "?open_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName() + "&pay_way=alipay,wx,qq" + "&full_screen";
                    }
                    if (temp.contains("fanxiao/final.html")) {
                        temp += "?token=" + PreferenceUtils.getToken()
                                + "&full_screen";
                    }
                    if (temp.contains("fanxiao/paihang.html")) {
                        temp += "?token=" + PreferenceUtils.getToken();
                    }
                    if (temp.contains("game_1.0")) {
                        temp += "&token=" + PreferenceUtils.getToken() + "&version=" + AppSetting.VERSION_CODE + "&userId=" + PreferenceUtils.getUUid() + "&channel=" + AppSetting.CHANNEL;
                    }
                    Uri uri = Uri.parse(temp);
                    IntentUtils.toActivityFromUri(context, uri, v);

                } else {
                    mTvContent.setText(entity.getContent() + "");
                    Log.i("MoeMoeApplication", "setDialogView: " + entity.getContent());
                }
                if (entity.getDialogType() != null && entity.getDialogType().equals("dialog_option")) {
                    mRlSelect.setVisibility(View.VISIBLE);
                    for (int i = 0; i < entity.getOptions().size(); i++) {
                        if (i == 0) {
                            mIvLeft.setText(entity.getOptions().get(i).getOption()+"");
                        } else {
                            if (entity.getOptions().get(i) == null) {
                                mIvCansl.setVisibility(View.GONE);
                            } else {
                                mIvCansl.setText(entity.getOptions().get(i).getOption()+"");
                            }
                        }
                    }
                } else {
                    mRlSelect.setVisibility(View.GONE);
                }
            } else {
                dialog.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 解析台词层级
     *
     * @param mData
     * @return
     */
    private StageLineEntity getStageLineEntity(StageLineEntity mData, String isSelect) {
        List<StageLineOptionsEntity> options = mData.getOptions();
        String LeftId = null;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOption().equals(isSelect)) {
                LeftId = options.get(i).getId();
            }
        }
        List<StageLineEntity> children = mData.getChildren();
        if (children == null) {
            return null;
        } else {
            StageLineEntity stageLineEntity = new StageLineEntity();
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getParentOptionId().equals(LeftId)) {
                    stageLineEntity = children.get(i);
                }
            }
            return stageLineEntity;
        }
    }

    /**
     * 菜单栏的定位
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void windowManagerOnclick(int x, int y, int width, int height) {
        if (mRlRenWu != null && inflateTwo.getVisibility() == View.GONE) {
            inflateTwo.setVisibility(View.VISIBLE);
            int mapX = this.width;
            int mapY = this.height;
            int marginHeight = height + (int) getResources().getDimension(R.dimen.status_bar_height);
            if (x <= 0) {
                if (y == 0) {
                    mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                    layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                    mLlFrist.setLayoutParams(layoutParams);
                    wmParamsTwo.x = (int) (x + getResources().getDimension(R.dimen.x24));
                    wmParamsTwo.y = y + height - (int) getResources().getDimension(R.dimen.status_bar_height);
                    windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                } else {
                    if (y > mapY / 2) {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = (int) (x + getResources().getDimension(R.dimen.x24));
                        wmParamsTwo.y = (int) (y - marginHeight / 2 - getResources().getDimension(R.dimen.y24));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    } else {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = (int) (x + getResources().getDimension(R.dimen.x24));
                        wmParamsTwo.y = (int) (y + marginHeight / 2 - getResources().getDimension(R.dimen.y24));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    }
                }
            } else if (x >= mapX - width) {
                if (y > (mapY - getResources().getDimension(R.dimen.status_bar_height)) / 2) {
                    mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_right);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                    mLlFrist.setLayoutParams(layoutParams);
                    wmParamsTwo.x = (int) (x - getResources().getDimension(R.dimen.x428) + width - getResources().getDimension(R.dimen.x24));
                    wmParamsTwo.y = (int) (y - height / 2 - getResources().getDimension(R.dimen.y24));
                    windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                } else {
                    mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_right);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                    layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                    mLlFrist.setLayoutParams(layoutParams);
                    wmParamsTwo.x = (int) (x - (int) getResources().getDimension(R.dimen.x428) + width - getResources().getDimension(R.dimen.x24));
                    wmParamsTwo.y = (int) (y + height / 2 - getResources().getDimension(R.dimen.y24));
                    windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                }
            } else {
                if (y == 0) {
                    if (x > (mapX - width) / 2) {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_right);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = (int) (x - (width / 2));
                        wmParamsTwo.y = (int) (y + (height / 2) - getResources().getDimension(R.dimen.status_bar_height));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    } else {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_top_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.y48), 0, 0);
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = (int) (x - (int) getResources().getDimension(R.dimen.x24));
                        wmParamsTwo.y = (int) (y + (height / 2) - getResources().getDimension(R.dimen.status_bar_height));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    }
                } else if (y >= mapY - height) {
                    if (x > (mapX - width) / 2) {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_right);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = x - (width / 2);
                        wmParamsTwo.y = (int) (y - (height / 2));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    } else {
                        mRlRenWu.setBackgroundResource(R.drawable.bg_classmate_menu_bottom_left);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLlFrist.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.y48));
                        mLlFrist.setLayoutParams(layoutParams);
                        wmParamsTwo.x = x;
                        wmParamsTwo.y = (int) (y - (height / 2));
                        windowManager.updateViewLayout(inflateTwo, wmParamsTwo);
                    }
                }
            }
        } else {
            inflateTwo.setVisibility(View.GONE);
        }
    }

    /**
     * 对话框的定位
     */
    public void goDialog(Context context) {
        if (imageView != null && imageView.getVisibility() == View.GONE) {
            return;
        }

        int x = functionalX;
        int y = functionalY;
        int width = inflate.getMeasuredWidth();
        int height = inflate.getMeasuredHeight();
        if (dialog != null && dialog.getVisibility() == View.GONE) {
            dialog.setVisibility(View.VISIBLE);
            int mapX = this.width;
            int mapY = this.height;
            int marginHeight = height + (int) getResources().getDimension(R.dimen.status_bar_height);
            if (x <= 0) {
                if (y == 0) {
                    mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_left);
                    wmParamsThree.x = (int) (x + getResources().getDimension(R.dimen.x24));
                    wmParamsThree.y = y + height - (int) getResources().getDimension(R.dimen.status_bar_height);
                    windowManager.updateViewLayout(dialog, wmParamsThree);
                } else {
                    if (y > mapY / 2) {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_left);
                        wmParamsThree.x = (int) (x + getResources().getDimension(R.dimen.x24));
                        wmParamsThree.y = (int) (y - marginHeight / 2 - getResources().getDimension(R.dimen.y24));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    } else {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_left);
                        wmParamsThree.x = (int) (x + getResources().getDimension(R.dimen.x24));
                        wmParamsThree.y = (int) (y + marginHeight / 2 - getResources().getDimension(R.dimen.y24));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    }
                }
            } else if (x >= mapX - width) {
                if (y > (mapY - getResources().getDimension(R.dimen.status_bar_height)) / 2) {
                    mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_right);
                    wmParamsThree.x = (int) (x - getResources().getDimension(R.dimen.x428) + width - getResources().getDimension(R.dimen.x24));
                    wmParamsThree.y = (int) (y - height / 2 - getResources().getDimension(R.dimen.y24));
                    windowManager.updateViewLayout(dialog, wmParamsThree);
                } else {
                    mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_right);
                    wmParamsThree.x = (int) (x - (int) getResources().getDimension(R.dimen.x428) + width - getResources().getDimension(R.dimen.x24));
                    wmParamsThree.y = (int) (y + height / 2 - getResources().getDimension(R.dimen.y24));
                    windowManager.updateViewLayout(dialog, wmParamsThree);
                }
            } else {
                if (y == 0) {
                    if (x > (mapX - width) / 2) {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_right);
                        wmParamsThree.x = (int) (x - (width / 2));
                        wmParamsThree.y = (int) (y + (height / 2) - getResources().getDimension(R.dimen.status_bar_height));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    } else {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_top_left);
                        wmParamsThree.x = (int) (x + (int) getResources().getDimension(R.dimen.x24));
                        wmParamsThree.y = (int) (y + (height / 2) - getResources().getDimension(R.dimen.status_bar_height));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    }
                } else if (y >= mapY - height) {
                    if (x > (mapX - width) / 2) {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_right);
                        wmParamsThree.x = x - width;
                        wmParamsThree.y = (int) (y - (height / 2));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    } else {
                        mRlDialog.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_left);
                        wmParamsThree.x = x;
                        wmParamsThree.y = (int) (y - (height / 2));
                        windowManager.updateViewLayout(dialog, wmParamsThree);
                    }
                }
            }

            String stageLine = PreferenceUtils.getStageLine(context);
            Gson gson = new Gson();
            stageLineEntity = gson.fromJson(stageLine, StageLineEntity.class);
            setDialogView(context, stageLineEntity, dialog);
        } else {
            dialog.setVisibility(View.GONE);
        }
    }

    /**
     * 数据压制
     */
    public int goGreenDao(Context context) {
        ArrayList<DeskmateEntils> entilsDao = (ArrayList<DeskmateEntils>) GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao()
                .queryBuilder()
                .where(DeskmateEntilsDao.Properties.Type.eq("HousUser"))
                .list();
        if (entilsDao != null && entilsDao.size() > 0) {
            for (DeskmateEntils entils : entilsDao) {
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
//        if (entilsDao != null && entilsDao.size() > 0) {
//            Drawable drawable;
//            if (!TextUtils.isEmpty(entilsRight.getPath())) {
//                drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
//            } else {
//                drawable = ContextCompat.getDrawable(context, R.drawable.btn_classmate_len_right);
//            }
//            if (drawable != null) {
//                imageView.setImageDrawable(drawable);
//            } else {
//                FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entilsRight.getFileName());
//            }
//            functionalX = width - (int) getResources().getDimension(R.dimen.x280);
//            functionalY = height / 2 - (int) getResources().getDimension(R.dimen.x180);
//            wmParams.x = width - (int) getResources().getDimension(R.dimen.x280);
//            wmParams.y = height / 2 - (int) getResources().getDimension(R.dimen.x180);
//            windowManager.updateViewLayout(inflate, wmParams);
//        }
        return entilsDao.size() > 0 ? entilsDao.size() : 0;
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
