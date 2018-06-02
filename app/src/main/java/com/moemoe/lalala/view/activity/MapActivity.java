package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liulishuo.filedownloader.FileDownloader;
import com.moemoe.lalala.BuildConfig;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.AppSetting;
import com.moemoe.lalala.app.AppStatusConstant;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerMapComponent;
import com.moemoe.lalala.di.modules.MapModule;
import com.moemoe.lalala.event.BackSchoolEvent;
import com.moemoe.lalala.event.EventDoneEvent;
import com.moemoe.lalala.event.MateChangeEvent;
import com.moemoe.lalala.event.SystemMessageEvent;
import com.moemoe.lalala.greendao.gen.AlarmClockEntityDao;
import com.moemoe.lalala.model.entity.AlarmClockEntity;
import com.moemoe.lalala.model.entity.AppUpdateEntity;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.model.entity.BuildEntity;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.model.entity.DeskmateImageEntity;
import com.moemoe.lalala.model.entity.JuQIngStoryEntity;
import com.moemoe.lalala.model.entity.JuQingDoneEntity;
import com.moemoe.lalala.model.entity.JuQingTriggerEntity;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.model.entity.MapMarkEntity;
import com.moemoe.lalala.model.entity.NearUserEntity;
import com.moemoe.lalala.model.entity.NetaEvent;
import com.moemoe.lalala.model.entity.SplashEntity;
import com.moemoe.lalala.model.entity.UserDeskmateEntity;
import com.moemoe.lalala.model.entity.UserLocationEntity;
import com.moemoe.lalala.presenter.MapContract;
import com.moemoe.lalala.presenter.MapPresenter;
import com.moemoe.lalala.rongyun.MoeMoeImagePlugin;
import com.moemoe.lalala.service.DaemonService;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.JuQingUtil;
import com.moemoe.lalala.utils.MapEevent;
import com.moemoe.lalala.utils.MapToolTipUtils;
import com.moemoe.lalala.utils.MapUtil;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.SplashUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.map.MapWidget;
import com.moemoe.lalala.view.widget.map.config.OfflineMapConfig;
import com.moemoe.lalala.view.widget.map.events.MapTouchedEvent;
import com.moemoe.lalala.view.widget.map.events.ObjectTouchEvent;
import com.moemoe.lalala.view.widget.map.interfaces.Layer;
import com.moemoe.lalala.view.widget.map.interfaces.MapEventsListener;
import com.moemoe.lalala.view.widget.map.interfaces.OnMapTilesFinishedLoadingListener;
import com.moemoe.lalala.view.widget.map.interfaces.OnMapTouchListener;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;

/**
 * 地图主界面
 * Created by yi on 2016/11/27.
 */

public class MapActivity extends BaseAppCompatActivity implements MapContract.View, IUnReadMessageObserver, TencentLocationListener {

    private static final int MAP_SCHOOL_ASA = 0;
    private static final int MAP_SCHOOL_YORU = 1;
    private static final int MAP_SCHOOL_GOGO = 3;
    private static final int MAP_SCHOOL_MAYONAKA = 4;
    private static final int MAP_SCHOOL_SYOUGO = 5;
    private static final int MAP_SCHOOL_TASOGARE = 6;

    @BindView(R.id.main_root)
    RelativeLayout mMainRoot;
    @BindView(R.id.fl_map_root)
    FrameLayout mMap;
    @BindView(R.id.rl_main_list_root)
    View mPhoneRoot;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.tv_sys_msg)
    TextView mTvSysMsg;
    @BindView(R.id.iv_role)
    ImageView mIvRole;
    @BindView(R.id.iv_create_dynamic)
    ImageView mIvCreatDynamic;
    @BindView(R.id.tv_show_live2d)
    TextView mTvShowLiv2d;
    @BindView(R.id.tv_show_text)
    TextView mTvText;
    @BindView(R.id.rl_role_root)
    RelativeLayout mRoleRoot;
    @BindView(R.id.tv_sys_time)
    TextView mTvTime;
    @BindView(R.id.iv_live2d)
    View mLive2dRoot;
    @BindView(R.id.iv_refresh)
    View mRefreshRoot;
    @BindView(R.id.iv_user_image)
    View mUserImageRoot;
    @BindView(R.id.rl_luntan_root)
    View mLuntanRoot;
    @BindView(R.id.rl_map_new)
    View mMapNew;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.ll_tool_bar)
    LinearLayout mLlToolBar;

    @BindView(R.id.tv_search)
    TextView mTvSearch;

    @Inject
    MapPresenter mPresenter;
    private MapWidget mapWidget;// 0 map 1 event 2 allUser 3 birthdayUser 4 followUser 5 nearUser
    private int mMapState = MAP_SCHOOL_ASA;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private boolean isGisterReciver;
    private long mLastBackTime = 0;
    private String mSchema;
    public static String updateApkName;
    private boolean mIsOut = false;
    public static long mUpdateDownloadId = Integer.MIN_VALUE;
    private MapMarkContainer mContainer;

    private BottomMenuFragment menuFragment;
    private Disposable initDisposable;
    private Disposable resolvDisposable;
    private TencentLocationManager locationManager;
    private UserDeskmateEntity deskmateEntity;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_map;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), null);
        AppSetting.isRunning = true;
        Intent intent = getIntent();
        if (intent != null) {
            mSchema = intent.getStringExtra("schema");
        }
        DaggerMapComponent.builder()
                .mapModule(new MapModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        FileDownloader.getImpl().bindService();
        mContainer = new MapMarkContainer();
        MapToolTipUtils.getInstance().init(this, 5, 8, mapWidget, mMap);
        initMap("map_asa");
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        startService(new Intent(this, DaemonService.class));
        if (!isGisterReciver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            registerReceiver(mReceiver, filter);
            isGisterReciver = true;
        }
        if (NetworkUtils.isNetworkAvailable(this) && NetworkUtils.isWifi(this)) {
            mPresenter.checkVersion();
        }
        mPresenter.getEventList();
        mPresenter.loadMapPics();
        String mTime = dateFormat.format(new Date());
        mTvTime.setText(mTime);
        EventBus.getDefault().register(this);
        ViewUtils.setRoleButton(mIvRole, mTvText);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(60 * 1000 * 60 * 2);//1小时获取一次定位
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO);
        request.setAllowCache(true);
        locationManager = TencentLocationManager.getInstance(this);
        locationManager.requestLocationUpdates(request, this);
        mPresenter.loadSplashList();
        mPresenter.loadMapAllUser();
        mPresenter.loadMapBirthdayUser();
        mPresenter.loadMapTopUser();
//        if (MoeMoeApplication.getInstance().isWindow()) {
//            MoeMoeApplication.getInstance().initWindowManager(this, getWindowManager());
//        }

        Uri uri = intent.getData();
        if (uri != null) {
            String userId = uri.getQueryParameter("UserId");
            showToast(userId);
            Intent intent1 = new Intent(MapActivity.this, NewDocDetailActivity.class);
            intent.putExtra("uuid", userId);
            startActivity(intent1);
        }
    }

    private void refreshMap() {
        mPresenter.loadMapAllUser();
        mPresenter.loadMapBirthdayUser();
        mPresenter.loadMapTopUser();
        mPresenter.addMapMark(this, mContainer, mapWidget, "nearUser");
        if (PreferenceUtils.isLogin() && !AppSetting.isLoadDone) {
            mPresenter.findMyDoneJuQing();
        } else {
            mPresenter.loadMapEachFollowUser();
            mPresenter.checkStoryVersion();
        }
    }

    private void initMap(String map) {
        mapWidget = new MapWidget(this, map, 12);
        mapWidget.centerMap();
        OfflineMapConfig config = mapWidget.getConfig();
        mapWidget.scrollMapTo(0, 0);
        config.setPinchZoomEnabled(true);
        config.setFlingEnabled(true);
        config.setMaxZoomLevelLimit(14);
        config.setMinZoomLevelLimit(12);
        config.setZoomBtnsVisible(false);
        config.setMapCenteringEnabled(true);
        mMap.removeAllViews();
        mMap.addView(mapWidget);
        mPresenter.addMapMark(this, mContainer, mapWidget, "map");
        mPresenter.addMapMark(this, mContainer, mapWidget, "allUser");
        mPresenter.addMapMark(this, mContainer, mapWidget, "birthdayUser");
        mPresenter.addMapMark(this, mContainer, mapWidget, "followUser");
        mPresenter.addMapMark(this, mContainer, mapWidget, "nearUser");
        MapToolTipUtils.getInstance().updateMap(mapWidget);
        MapToolTipUtils.getInstance().updateList(mContainer.getContainer());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MoeMoeApplication.getInstance().GoneWindowMager(this);
        hideBtn();
        MapToolTipUtils.getInstance().stop();
    }

    private void clearMap() {
        if (mapWidget != null) {
            mContainer = new MapMarkContainer();
            mapWidget.clearLayers();
            mapWidget = null;
        }
    }

    private void requestEvent() {
        if (PreferenceUtils.isLogin() && !AppSetting.isLoadDone) {
            mPresenter.findMyDoneJuQing();
        } else {
            mPresenter.checkStoryVersion();
        }
    }

    private boolean checkHasLayer(long id) {
        return mapWidget.getLayerById(id) != null;
    }

    private void invalidateMap(boolean shouldChange) {
        if (StringUtils.isasa()) {
            if (mMapState != MAP_SCHOOL_ASA || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_ASA;
                initMap("map_asa");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
        if (StringUtils.issyougo()) {
            if (mMapState != MAP_SCHOOL_SYOUGO || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_SYOUGO;
                initMap("map_syougo");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
        if (StringUtils.isgogo()) {
            if (mMapState != MAP_SCHOOL_GOGO || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_GOGO;
                initMap("map_gogo");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
        if (StringUtils.istasogare()) {
            if (mMapState != MAP_SCHOOL_TASOGARE || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_TASOGARE;
                initMap("map_tasogare");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
        if (StringUtils.isyoru2()) {
            if (mMapState != MAP_SCHOOL_YORU || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_YORU;
                initMap("map_yoru");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
        if (StringUtils.ismayonaka()) {
            if (mMapState != MAP_SCHOOL_MAYONAKA || shouldChange) {
                boolean checkHasLayer = checkHasLayer(1);
                clearMap();
                mMapState = MAP_SCHOOL_MAYONAKA;
                initMap("map_mayonaka");
                initMapListeners();
                if (checkHasLayer) {
                    requestEvent();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MoeMoeApplication.getInstance().VisibilityWindowMager(this);
        showBtn();
        MapToolTipUtils.getInstance().start();
        if (PreferenceUtils.getMessageDot(this, "neta") || PreferenceUtils.getMessageDot(this, "system") || PreferenceUtils.getMessageDot(this, "at_user") || PreferenceUtils.getMessageDot(this, "normal")) {//|| showDot){
            int num = PreferenceUtils.getNetaMsgDotNum(this)
                    + PreferenceUtils.getSysMsgDotNum(this)
                    + PreferenceUtils.getAtUserMsgDotNum(this)
                    + PreferenceUtils.getNormalMsgDotNum(this);
            if (num > 999) num = 999;
            mTvSysMsg.setText(num + "条通知");
            mTvSysMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_inform_reddot), null, null, null);
            mTvSysMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
        } else {
            mTvSysMsg.setText("无新通知");
            mTvSysMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mTvSysMsg.setCompoundDrawablePadding(0);
        }
        invalidateMap(false);
        ViewUtils.setRoleButton(mIvRole, mTvText);
        String mTime = dateFormat.format(new Date());
        mTvTime.setText(mTime);
        if (PreferenceUtils.isLogin() && !AppSetting.isLoadDone) {
            mPresenter.findMyDoneJuQing();
            mPresenter.loadMapEachFollowUser();
        } else {
            mPresenter.checkStoryVersion();
        }
        int dotNum = PreferenceUtils.getGroupDotNum(this) + PreferenceUtils.getRCDotNum(this) + PreferenceUtils.getJuQIngDotNum(this);
        if (dotNum > 0) {
            if (dotNum > 999) dotNum = 999;
            mTvMsg.setText(dotNum + "条新聊天");
            mTvMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_inform_reddot), null, null, null);
            mTvMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
        } else {
            mTvMsg.setText("无新聊天");
            mTvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mTvMsg.setCompoundDrawablePadding(0);
        }
        if (PreferenceUtils.isLogin() && !PreferenceUtils.isSetAlarm(this)) {
            AlarmClockEntity mAlarmClock = new AlarmClockEntity();
            mAlarmClock.setId(-1);
            mAlarmClock.setOnOff(true); // 闹钟默认开启
            mAlarmClock.setRepeat("只响一次");
            mAlarmClock.setWeeks(null);
            mAlarmClock.setRoleName("小莲");
            mAlarmClock.setRoleId("len");
            mAlarmClock.setRingName("按时休息");
            mAlarmClock.setRingUrl(R.raw.vc_alerm_len_sleep_1);
            mAlarmClock.setHour(22);
            mAlarmClock.setMinute(0);
            AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
            if (mAlarmClock.getId() == -1) {
                AlarmClockEntity entity = dao.queryBuilder().orderDesc(AlarmClockEntityDao.Properties.Id).limit(1).unique();
                long id;
                if (entity == null) {
                    id = 0;
                } else {
                    id = entity.getId();
                }
                mAlarmClock.setId(id + 1);
            }
            dao.insertOrReplace(mAlarmClock);
            PreferenceUtils.setAlarm(this, true);
        }
        if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            if (!TextUtils.isEmpty(PreferenceUtils.getAuthorInfo().getRcToken())) {
                RongIM.connect(PreferenceUtils.getAuthorInfo().getRcToken(), new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        mPresenter.loadRcToken();
                    }

                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            } else {
                mPresenter.loadRcToken();
            }
        }
//        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
//            mIvPresonal.setImageResource(R.drawable.bg_default_circle);
//        } else {
//            int size = (int) getResources().getDimension(R.dimen.x64);
//            Glide.with(this)
//                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
//                    .error(R.drawable.bg_default_circle)
//                    .placeholder(R.drawable.bg_default_circle)
//                    .bitmapTransform(new CropCircleTransformation(this))
//                    .into(mIvPresonal);
//        }
        mPresenter.loadHousUserDeskmate();
    }

    @Override
    public void onGetTimeSuccess(Date time) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Observable.create(new ObservableOnSubscribe<ArrayList<JuQingTriggerEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<JuQingTriggerEntity>> e) throws Exception {
                ArrayList<JuQingTriggerEntity> list = JuQingUtil.checkJuQingAll(calendar);
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<JuQingTriggerEntity>>() {
                    @Override
                    public void accept(ArrayList<JuQingTriggerEntity> id) throws Exception {
                        Layer layer = mapWidget.getLayerById(1);
                        // mContainerEvent = null;
                        if (layer != null) {
                            layer.clearAll();
                            mapWidget.removeLayer(1);
                        }
                        mapWidget.createLayer(1);
                        PreferenceUtils.setJuQingDotNum(MapActivity.this, 0);
                        if (PreferenceUtils.isLogin()) {
                            for (JuQingTriggerEntity entity : id) {
                                if (entity.getType().equals("mobile")) {
                                    if (entity.isForce()) {
                                        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                                .appendPath("conversation").appendPath("private")
                                                .appendQueryParameter("targetId", "juqing:" + entity.getStoryId() + ":" + entity.getRoleOf()).build();
                                        Intent i2 = new Intent(MapActivity.this, PhoneMainV2Activity.class);
                                        i2.setData(uri);
                                        startActivity(i2);
                                    }
                                    int dotNum = PreferenceUtils.getJuQIngDotNum(MapActivity.this) + 1;
                                    PreferenceUtils.setJuQingDotNum(MapActivity.this, dotNum);
                                    dotNum += PreferenceUtils.getRCDotNum(MapActivity.this) + PreferenceUtils.getGroupDotNum(MapActivity.this);
                                    if (dotNum > 999) dotNum = 999;
                                    mTvMsg.setText(dotNum + "条新聊天");
                                    mTvMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_inform_reddot), null, null, null);
                                    mTvMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
                                }
                                if (entity.getType().equals("map")) {
                                    String extra = entity.getExtra();
                                    boolean isForce = entity.isForce();
                                    JsonObject jsonObject = new Gson().fromJson(extra, JsonObject.class);
                                    String eventId = jsonObject.get("map").getAsString();
                                    String icon = jsonObject.get("icon").getAsString();
                                    mPresenter.addEventMark(eventId, icon, mContainer, MapActivity.this, mapWidget, entity.getStoryId());
                                    if (isForce) {
                                        Intent i = new Intent(MapActivity.this, MapEventNewActivity.class);
                                        i.putExtra("id", entity.getStoryId());
                                        startActivity(i);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onGetTriggerSuccess(ArrayList<JuQingTriggerEntity> entities) {
        JuQingUtil.saveJuQingTriggerList(entities);
    }

    @Override
    public void onGetAllStorySuccess(ArrayList<JuQIngStoryEntity> entities) {
        JuQingUtil.saveJuQingStoryList(entities);
    }

    @Override
    public void onCheckStoryVersionSuccess(int version) {
        int version1 = PreferenceUtils.getJuQingVersion(this);
        if (version1 < version) {
            mPresenter.getAllStory();
            mPresenter.getTrigger();
            PreferenceUtils.setJuQingVersion(this, version);
        }
        mPresenter.getServerTime();
    }

    @Override
    public void onFindMyDoneJuQingSuccess(ArrayList<JuQingDoneEntity> entities) {
        JuQingUtil.saveJuQingDone(entities);
        AppSetting.isLoadDone = true;
        mPresenter.checkStoryVersion();
    }

    @Override
    public void getEventSuccess(ArrayList<NetaEvent> events) {
        for (NetaEvent event : events) {
            if (event.getSign().equals("BS")) {
                if (!TextUtils.isEmpty(event.getSchedule()))
                    PreferenceUtils.setBackSchoolLevel(this, Integer.valueOf(event.getSchedule()));
            }
        }
    }

    @Override
    public void saveEventSuccess() {

    }

    private void initMapListeners() {
        mapWidget.setOnMapTouchListener(new OnMapTouchListener() {
            @Override
            public void onTouch(MapWidget v, MapTouchedEvent event) {
                List<ObjectTouchEvent> objectTouchEvents = event.getTouchedObjectEvents();
                if (objectTouchEvents.size() == 0) {
                    if (!MoeMoeApplication.getInstance().isMenu()) {
                        if (mIsOut) {
                            imgIn();
                            mIsOut = false;
                        } else {
                            imgOut();
                            mIsOut = true;
                        }
                    } else {
                        clickSelect();
                    }
                } else {
                    if (MoeMoeApplication.getInstance().isMenu()) {
                        clickSelect();
                        return;
                    }
                }
                if (objectTouchEvents.size() == 1) {
                    ObjectTouchEvent objectTouchEvent = objectTouchEvents.get(0);
                    Object objectId = objectTouchEvent.getObjectId();
                    MapMarkEntity entity = mContainer.getMarkById((String) objectId);
                    // MapMarkEntity entity1 = null;
                    //  if(mContainerEvent!=null)entity1 = mContainerEvent.getMarkById((String) objectId);
//                    if(entity1!=null) {
//                        entity = entity1;
//                    }else {
//                        entity = entity2;
//                    }

                    if (objectTouchEvent.getLayerId() == 0) {
                        //埋点统计：用id做判断点击的是哪个位置
                        String id = entity.getId();
                        clickEvent(id);
                    } else if (objectTouchEvent.getLayerId() == 2) {
                        //埋点统计：用id做判断点击的是哪个位置
                        clickEvent("全部用户的位置");
                    } else if (objectTouchEvent.getLayerId() == 3) {
                        clickEvent("生日派对区域");
                    } else if (objectTouchEvent.getLayerId() == 4) {
                        clickEvent("我的好友区域");
                    } else if (objectTouchEvent.getLayerId() == 5) {
                        clickEvent("附近的人");
                    } else if (objectTouchEvent.getLayerId() == 6) {
                        clickEvent("最佳形象区域");
                    } else {
                        //埋点统计：用id做判断点击的是哪个位置
                        String id = entity.getId();
                        clickEvent(id);
                    }

                    if (!TextUtils.isEmpty(entity.getSchema())) {
                        String temp = entity.getSchema();
                        if (temp.contains("map_event_1.0") || temp.contains("game_1.0")) {
                            if (!DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
                                return;
                            }
                        }

                        if (entity.getId().equals("恋爱讲座")) {
                            if (menuFragment == null) {
                                ArrayList<MenuItem> items = new ArrayList<>();
                                MenuItem item = new MenuItem(0, "赤印");
                                items.add(item);
                                item = new MenuItem(1, "雪之本境");
                                items.add(item);
                                item = new MenuItem(2, "且听琴语");
                                items.add(item);
                                menuFragment = new BottomMenuFragment();
                                menuFragment.setShowTop(true);
                                menuFragment.setTopContent("选择听哪个故事呢？");
                                menuFragment.setMenuItems(items);
                                menuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
                                menuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
                                    @Override
                                    public void OnMenuItemClick(int itemId) {
                                        String url = "";
                                        if (itemId == 0) {
                                            url = "https://www.iqing.in/play/653";
                                        } else if (itemId == 1) {
                                            url = "https://www.iqing.in/play/654";
                                        } else if (itemId == 2) {
                                            url = "https://www.iqing.in/play/655";
                                        }
                                        WebViewActivity.startActivity(MapActivity.this, url, true);
                                    }
                                });
                            }
                            menuFragment.show(getSupportFragmentManager(), "mapMenu");
                        } else if (entity.getId().equals("扭蛋机抽奖")) {
                            if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {

                                //埋点统计：用id做判断点击的是哪个位置
                                String id = entity.getId();
                                clickEvent(id);

                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                                try {
                                    temp += "?user_id=" + authorInfo.getUserId()
                                            + "&nickname=" + (TextUtils.isEmpty(authorInfo.getUserName()) ? "" : URLEncoder.encode(authorInfo.getUserName(), "UTF-8"))
                                            + "&token=" + PreferenceUtils.getToken();
                                    Uri uri = Uri.parse(temp);
                                    IntentUtils.haveShareWeb(MapActivity.this, uri, v);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (temp.contains("http://s.moemoe.la/game/devil/index.html")) {
                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                                temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
                            }
//                            if (temp.contains("http://192.168.1.186:8020/ceshiN/index.html")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
//                            }
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
                                temp += "?pass=" + PreferenceUtils.getPassEvent(MapActivity.this) + "&user_id=" + authorInfo.getUserId();
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
                            IntentUtils.toActivityFromUri(MapActivity.this, uri, v);
                        }
                    }
                }
            }
        });
        mapWidget.addMapEventsListener(new MapEventsListener() {
            @Override
            public void onPreZoomIn() {
            }

            @Override
            public void onPostZoomIn() {
            }

            @Override
            public void onPreZoomOut() {
            }

            @Override
            public void onPostZoomOut() {
            }
        });
        mapWidget.setOnMapTilesFinishLoadingListener(new OnMapTilesFinishedLoadingListener() {
            @Override
            public void onMapTilesFinishedLoading() {

            }
        });


        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSelect();
                //埋点统计：home
                startActivity(new Intent(MapActivity.this, DormitoryActivity.class));
            }
        });
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSelect();
                //埋点统计：社团
                Intent i3 = new Intent(MapActivity.this, FeedV3Activity.class);
                startActivity(i3);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void backSchoolEvent(BackSchoolEvent event) {
        mPresenter.saveEvent(new NetaEvent(event.getPass() + "", "BS"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mateChangeEvent(MateChangeEvent event) {
        ViewUtils.setRoleButton(mIvRole, mTvText);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDoneEvent(EventDoneEvent eventDoneEvent) {
        if (eventDoneEvent.getType().equals("mobile")) {
            int dotNum = PreferenceUtils.getJuQIngDotNum(MapActivity.this);
            dotNum += PreferenceUtils.getRCDotNum(MapActivity.this) + PreferenceUtils.getGroupDotNum(MapActivity.this);
            if (dotNum > 0) {
                if (dotNum > 999) dotNum = 999;
                mTvMsg.setText(dotNum + "条新聊天");
                mTvMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_inform_reddot), null, null, null);
                mTvMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
            } else {
                mTvMsg.setText("无新聊天");
                mTvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mTvMsg.setCompoundDrawablePadding(0);
            }
        } else if (eventDoneEvent.getType().equals("map")) {
            Layer layer = mapWidget.getLayerById(1);
            layer.setVisible(false);
            mapWidget.invalidate();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void systemMsgEvent(SystemMessageEvent event) {
        int num = PreferenceUtils.hasMsg(this);
        if (num > 0) {
            mTvSysMsg.setVisibility(View.GONE);
            if (num > 999) num = 999;
            mTvSysMsg.setText(num + "条通知");
            mTvSysMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_inform_reddot), null, null, null);
            mTvSysMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    public void checkBuildSuccess(BuildEntity s) {
    }

    @Override
    protected void initListeners() {
        initMapListeners();
        if (PreferenceUtils.isActivityFirstLaunch(this, "map")) {
            PreferenceUtils.setActivityFirstLaunch(this, "map", false);
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("当前应用缺少开启悬浮框权限。请点击\"设置\"-\"权限\"-打开所需权限。");
                    //拒绝，退出应用
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(MapActivity.this, "“(´・ω・`)那个人好奇怪，不给权限还想找资源…”\n" +
//                                            "\n" +
//                                            "“就是说啊，也不知道怎么想的…눈_눈”", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String sdk = android.os.Build.VERSION.SDK; // SDK号  

                                    String model = android.os.Build.MODEL; // 手机型号  

                                    String release = android.os.Build.VERSION.RELEASE; // android系统版本号  
                                    String brand = Build.BRAND;//手机厂商  
                                    if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
                                        gotoMiuiPermission();//小米  
                                    } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
                                        gotoMeizuPermission();
                                    } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
                                        gotoHuaweiPermission();
                                    } else {
                                        startActivity(getAppDetailSettingIntent());
                                    }
//                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"));
//                                    startActivityForResult(intent, request_code);
                                }
                            });
                    builder.setCancelable(false);
                    builder.show();
                }
            }

//            Intent i = new Intent(this, MengXinActivity.class);
//            i.putExtra("type", "map");
//            ArrayList<String> res = new ArrayList<>();
//            res.add("1.jpg");
//            res.add("2.jpg");
//            res.add("3.jpg");
//            res.add("4.jpg");
//            res.add("5.jpg");
//            res.add("6.jpg");
//            res.add("7.jpg");
//            res.add("8.jpg");
//            res.add("9.jpg");
//            res.add("10.jpg");
//            res.add("11.jpg");
//            res.add("12.jpg");
//            i.putExtra("gui", res);
//            startActivity(i);
        } else {
            if (!TextUtils.isEmpty(mSchema)) {
                IntentUtils.toActivityFromUri(this, Uri.parse(mSchema), null);
            } else {
                // Intent i3 = new Intent(MapActivity.this,WallBlockActivity.class);
                Intent i3 = new Intent(MapActivity.this, FeedV3Activity.class);
                startActivity(i3);
            }
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，<span style="font-size:18px;">也可以先把用户引导到系统设置页面</span>）
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private void gotoMiuiPermission() {
        try { // MIUI 8  
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getPackageName());
            startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7  
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", getPackageName());
                startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情  
                startActivity(getAppDetailSettingIntent());
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission() {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(getAppDetailSettingIntent());
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission() {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理  
            intent.setComponent(comp);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(getAppDetailSettingIntent());
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onLoadMapPics(ArrayList<MapEntity> entities) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(entities, "map"), "map", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                invalidateMap(true);
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "map");
                }
            }
        });
    }

    @Override
    public void onLoadRcTokenSuccess(String token) {
        PreferenceUtils.getAuthorInfo().setRcToken(token);
        RongIM.connect(PreferenceUtils.getAuthorInfo().getRcToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void onLoadRcTokenFail(int code, String msg) {

    }

    @Override
    public void onLoadMapAllUser(ArrayList<MapEntity> entities) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(entities, "allUser"), "allUser", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "allUser");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "allUser");
                }
            }
        });
    }

    @Override
    public void onLoadMapBirthDayUser(ArrayList<MapEntity> entities) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(entities, "birthdayUser"), "birthdayUser", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "birthdayUser");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "birthdayUser");
                }
            }
        });
    }

    @Override
    public void onLoadMapEachFollowUser(ArrayList<MapEntity> entities) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(entities, "followUser"), "followUser", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "followUser");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "followUser");
                }
            }
        });
    }

    @Override
    public void onLoadMapTopUser(NearUserEntity resList) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();

        Gson gson = new Gson();
        String posStr = gson.toJson(resList.getPositionList());
        PreferenceUtils.setTopUserPosition(this, posStr);
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(resList.getUsers(), "topUser"), "topUser", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "topUser");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "topUser");
                }
            }
        });
    }

    @Override
    public void onLoadMapNearUser(NearUserEntity resList) {
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        final ArrayList<MapDbEntity> errorList = new ArrayList<>();

        Gson gson = new Gson();
        String posStr = gson.toJson(resList.getPositionList());
        PreferenceUtils.setNearPosition(this, posStr);
        MapUtil.checkAndDownload(this, true, MapDbEntity.toDb(resList.getUsers(), "nearUser"), "nearUser", new Observer<MapDbEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                initDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (mapDbEntity.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorList.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "nearUser");
                if (errorList.size() > 0) {
                    resolvErrorList(errorList, "nearUser");
                }
            }
        });
    }

    @Override
    public void onLoadSplashSuccess(ArrayList<SplashEntity> entities) {
        SplashUtils.updateSplash(entities);
    }

    private void resolvErrorList(ArrayList<MapDbEntity> errorList, final String type) {
        final ArrayList<MapDbEntity> errorListTmp = new ArrayList<>();
        final ArrayList<MapDbEntity> res = new ArrayList<>();
        MapUtil.checkAndDownload(this, false, errorList, type, new Observer<MapDbEntity>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                resolvDisposable = d;
            }

            @Override
            public void onNext(@NonNull MapDbEntity mapDbEntity) {
                File file = new File(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                String md5 = mapDbEntity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file)) || mapDbEntity.getDownloadState() == 3) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + mapDbEntity.getFileName());
                    errorListTmp.add(mapDbEntity);
                } else {
                    res.add(mapDbEntity);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getMapDbEntityDao().insertOrReplaceInTx(res);
                if (errorListTmp.size() > 0) {
                    resolvErrorList(errorListTmp, type);
                } else {
                    if ("map".equals(type)) {
                        invalidateMap(true);
                    } else if ("allUser".equals(type)) {
                        mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "allUser");
                    } else if ("birthdayUser".equals(type)) {
                        mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "birthdayUser");
                    } else if ("followUser".equals(type)) {
                        mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "followUser");
                    } else if ("nearUser".equals(type)) {
                        mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "nearUser");
                    } else if ("topUser".equals(type)) {
                        mPresenter.addMapMark(MapActivity.this, mContainer, mapWidget, "topUser");
                    }
                }
            }
        });
    }

    private void resolvErrorListDeskmate(ArrayList<DeskmateEntils> errorList, final String type) {
        final ArrayList<DeskmateEntils> errorListTmp = new ArrayList<>();
        final ArrayList<DeskmateEntils> res = new ArrayList<>();
        MapUtil.checkAndDownloadDeskmate(this, false, errorList, "HousUser", new Observer<DeskmateEntils>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                resolvDisposable = d;
            }

            @Override
            public void onNext(@NonNull DeskmateEntils entils) {
                File file = new File(StorageUtils.getMapRootPath() + entils.getFileName());
                String md5 = entils.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file)) || entils.getDownloadState() == 3) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + entils.getFileName());
                    errorListTmp.add(entils);
                } else {
                    res.add(entils);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao().insertOrReplaceInTx(res);
                if (errorListTmp.size() > 0) {
                    resolvErrorListDeskmate(errorListTmp, type);
                } else {
                    if ("HousUser".equals(type)) {
                        MoeMoeApplication.getInstance().goGreenDao();
                    }
                }
            }
        });
    }

    @Override
    public void showUpdateDialog(final AppUpdateEntity entity) {
        if (this.isFinishing()) return;
        final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
        alertDialogUtil.createPromptDialog(this, entity.getTitle(), entity.getContent());
        alertDialogUtil.setButtonText(getString(R.string.label_update), getString(R.string.label_later), entity.getUpdateStatus());
        alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
            @Override
            public void CancelOnClick() {
                alertDialogUtil.dismissDialog();
            }

            @Override
            public void ConfirmOnClick() {
                alertDialogUtil.dismissDialog();
                try {
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(entity.getUrl());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    updateApkName = "neta-update" + new Date().getTime() + ".apk";
                    request.setDestinationInExternalFilesDir(MapActivity.this, null, updateApkName);
                    mUpdateDownloadId = downloadManager.enqueue(request);
                } catch (Throwable t) {
                    t.printStackTrace();
                    showToast(R.string.label_error_storage);
                }
            }
        });
        alertDialogUtil.showDialog();
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(MapActivity.this, code, msg);
    }

    @OnClick({R.id.rl_main_list_root, R.id.tv_search, R.id.iv_create_dynamic, R.id.iv_create_wenzhang, R.id.iv_role, R.id.iv_live2d, R.id.rl_luntan_root, R.id.rl_map_search, R.id.rl_map_refresh, R.id.tv_show_live2d})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_main_list_root:

                //埋点统计：手机
                clickEvent("手机");

                Intent i2 = new Intent(MapActivity.this, PhoneMainV2Activity.class);
                //i2.putExtra("have_dot",!mTvMsg.getText().toString().equals("无新信息"));
                startActivity(i2);
                break;
            case R.id.iv_create_dynamic:
                if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
                    //埋点统计：同桌小人——发布动态
                    clickEvent("同桌小人——发布动态");

                    clickRole();
                    Intent i4 = new Intent(MapActivity.this, CreateDynamicActivity.class);
                    i4.putExtra("default_tag", "广场");
                    startActivity(i4);
                }
                break;
            case R.id.iv_create_wenzhang:
                clickRole();
                Intent intent = new Intent(MapActivity.this, CreateRichDocActivity.class);
                intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 3);
                intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, "书包");
                intent.putExtra("from_name", "书包");
                intent.putExtra("from_schema", "neta://com.moemoe.lalala/bag_2.0");
                startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
                break;
            case R.id.iv_role:
                clickRole();
                break;
            case R.id.iv_live2d:

                //埋点统计：陪伴模式
                clickEvent("陪伴模式");
                Intent i = new Intent(MapActivity.this, Live2dActivity.class);
                startActivity(i);
                // mOrientationListener.disable();
                break;
//            case R.id.iv_bag:
//                clickSelect();
//                if (NetworkUtils.checkNetworkAndShowError(this) && DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//                if (PreferenceUtils.getAuthorInfo().isOpenBag()) {
//                    Intent i4 = new Intent(this, NewBagV5Activity.class);
//                    i4.putExtra("uuid", PreferenceUtils.getUUid());
//                    startActivity(i4);
//                } else {
//                    Intent i4 = new Intent(this, BagOpenActivity.class);
//                    startActivity(i4);
//                }
//            }
//                break;
//            case R.id.ll_feed_v3_root:
//                clickSelect();
//                if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
////                    //埋点统计：动态
////                    clickEvent("我的地图形象");
////                    Intent i5 = new Intent(MapActivity.this, CreateMapImageActivity.class);
////                    startActivity(i5);
//     
//                    Intent i5 = new Intent(this, NewDynamicActivity.class);
//                    startActivity(i5);
//                }
//                break;
            case R.id.rl_luntan_root:
                clickSelect();
                //埋点统计：动态
//                clickEvent("社团");
                // Intent i3 = new Intent(MapActivity.this,WallBlockActivity.class);
                Intent i3 = new Intent(MapActivity.this, FeedV3Activity.class);
                startActivity(i3);
                break;
            case R.id.rl_map_search:
                //埋点统计：搜索
                if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
                    clickEvent("地图-搜索");
                    Intent i6 = new Intent(MapActivity.this, AllSearchActivity.class);
                    i6.putExtra("type", "all");
                    startActivity(i6);
                }
                break;
            case R.id.rl_map_refresh:
                //埋点统计：地图刷新
                clickEvent("地图刷新");
                refreshMap();
                break;
            case R.id.tv_show_live2d:
                //埋点统计：陪伴模式
                clickEvent("同桌-陪睡模式");
                Intent i8 = new Intent(MapActivity.this, Live2dActivity.class);
                startActivity(i8);
                // mOrientationListener.disable();
                break;
//            case R.id.iv_phone_menu:
//                clickSelect();
//                if (NetworkUtils.checkNetworkAndShowError(this) && DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//                    //埋点统计：通讯录
//                    clickEvent("通讯录");
//                    startActivity(new Intent(this, PhoneMenuV3Activity.class));
//                }
//                break;
//            case R.id.iv_msg:
//                clickSelect();
//                if (NetworkUtils.checkNetworkAndShowError(this) && DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//                    //埋点统计：手机聊天
//                    clickEvent("手机聊天");
//                    NoticeActivity.startActivity(MapActivity.this, 1);
//                }
//                break;
//            case R.id.iv_sign_root:
//                clickSelect();
//                if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//
//                    //埋点统计：手机每日任务
//                    clickEvent("手机每日任务");
//                    DailyTaskActivity.startActivity(this);
//                }
//                break;
//            case R.id.iv_shopping:
//                clickSelect();
//                //埋点统计：手机商店
//                clickEvent("手机商店");
//                Intent i7 = new Intent(MapActivity.this, CoinShopActivity.class);
//                startActivity(i7);
//                break;
//            case R.id.iv_personal:
//                if (NetworkUtils.checkNetworkAndShowError(MapActivity.this) && DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//                    //埋点统计：手机个人中心
//                    clickEvent("个人中心");
//                    Intent i1 = new Intent(MapActivity.this, PersonalV2Activity.class);
//                    i1.putExtra(UUID, PreferenceUtils.getUUid());
//                    startActivity(i1);
//                }
//                break;
            case R.id.tv_search:
                if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
                    clickEvent("地图-搜索");
                    Intent i6 = new Intent(MapActivity.this, AllSearchActivity.class);
                    i6.putExtra("type", "all");
                    startActivity(i6);
                }
                break;
        }
    }


    public void clickSelect() {
        if (MoeMoeApplication.getInstance().isMenu()) {
            MoeMoeApplication.getInstance().GoneMenu();
        }
//        if (mIvSelect.isSelected()) {
//            ViewGroup.LayoutParams layoutParams = mLlComprehensive.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            mLlComprehensive.setLayoutParams(layoutParams);
//            mLlComprehensive.setBackgroundColor(getResources().getColor(R.color.white_e5));
//            ViewGroup.LayoutParams params = mLlComprehensiveChild.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            mLlComprehensiveChild.setLayoutParams(params);
//            mLlComprehensiveChild.setVisibility(View.VISIBLE);
//            mLlFrist.setVisibility(View.VISIBLE);
//            mLlSecond.setVisibility(View.VISIBLE);
//        } else {
//            ViewGroup.LayoutParams layoutParams = mLlComprehensive.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mLlComprehensive.setLayoutParams(layoutParams);
//            mLlComprehensive.setBackgroundColor(getResources().getColor(R.color.transparent));
//            ViewGroup.LayoutParams params = mLlComprehensiveChild.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mLlComprehensiveChild.setLayoutParams(params);
//            mLlFrist.setVisibility(View.GONE);
//            mLlSecond.setVisibility(View.GONE);
//        }
    }

    private void clickRole() {
        mIvRole.setSelected(!mIvRole.isSelected());
        if (mIvRole.isSelected()) {
            mTvShowLiv2d.setVisibility(View.VISIBLE);
            mIvCreatDynamic.setVisibility(View.GONE);
            mTvText.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRoleRoot.setLayoutParams(lp);
            mRoleRoot.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.alpha_60));
            mRoleRoot.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //同桌小人
                    clickEvent("同桌小人");
                    clickRole();
                }
            });
            mTvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent("同桌-消息提示");
                }
            });
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp1.addRule(RelativeLayout.ALIGN_PARENT_END);
            mIvRole.setLayoutParams(lp1);
        } else {
            mTvShowLiv2d.setVisibility(View.GONE);
            mIvCreatDynamic.setVisibility(View.GONE);
            // mIvCreateWen.setVisibility(View.GONE);
            mTvText.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
            mRoleRoot.setLayoutParams(lp);
            mRoleRoot.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.transparent));
            mRoleRoot.setOnClickListener(null);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mIvRole.setLayoutParams(lp1);
        }
    }

    private void imgIn() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(mPhoneRoot, "translationY", mPhoneRoot.getHeight(), 0).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator luntanAnimator = ObjectAnimator.ofFloat(mLuntanRoot, "translationY", mLuntanRoot.getHeight(), 0).setDuration(300);
        luntanAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator roleAnimator = ObjectAnimator.ofFloat(mRoleRoot, "translationY", mRoleRoot.getHeight(), 0).setDuration(300);
        roleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator live2dAnimator = ObjectAnimator.ofFloat(mLive2dRoot, "translationY", -mLive2dRoot.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        live2dAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator refreshAnimator = ObjectAnimator.ofFloat(mRefreshRoot, "translationY", -mRefreshRoot.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        refreshAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator userImageAnimator = ObjectAnimator.ofFloat(mUserImageRoot, "translationY", -mUserImageRoot.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        userImageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mMapNewAnimator = ObjectAnimator.ofFloat(mMapNew, "translationY", -mMapNew.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mMapNewAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mToolBarAnimator = ObjectAnimator.ofFloat(mLlToolBar, "translationY", -mLlToolBar.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mToolBarAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(luntanAnimator);
        set.play(luntanAnimator).with(roleAnimator);
        set.play(roleAnimator).with(live2dAnimator);
        set.play(live2dAnimator).with(refreshAnimator);
        set.play(refreshAnimator).with(userImageAnimator);
        set.play(userImageAnimator).with(mMapNewAnimator);
        set.play(mMapNewAnimator).with(mToolBarAnimator);
        set.start();
    }

    private void imgOut() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(mPhoneRoot, "translationY", 0, mPhoneRoot.getHeight()).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator luntanAnimator = ObjectAnimator.ofFloat(mLuntanRoot, "translationY", 0, mLuntanRoot.getHeight()).setDuration(300);
        luntanAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator roleAnimator = ObjectAnimator.ofFloat(mRoleRoot, "translationY", 0, mRoleRoot.getHeight()).setDuration(300);
        roleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator live2dAnimator = ObjectAnimator.ofFloat(mLive2dRoot, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mLive2dRoot.getHeight()).setDuration(300);
        live2dAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator refreshAnimator = ObjectAnimator.ofFloat(mRefreshRoot, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mRefreshRoot.getHeight()).setDuration(300);
        refreshAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator userImageAnimator = ObjectAnimator.ofFloat(mUserImageRoot, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mUserImageRoot.getHeight()).setDuration(300);
        userImageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mMapNewAnimator = ObjectAnimator.ofFloat(mMapNew, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mMapNew.getHeight()).setDuration(300);
        mMapNewAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mToolBarAnimator = ObjectAnimator.ofFloat(mLlToolBar, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mLlToolBar.getHeight()).setDuration(300);
        mToolBarAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(luntanAnimator);
        set.play(luntanAnimator).with(roleAnimator);
        set.play(roleAnimator).with(live2dAnimator);
        set.play(live2dAnimator).with(refreshAnimator);
        set.play(refreshAnimator).with(userImageAnimator);
        set.play(userImageAnimator).with(mMapNewAnimator);
        set.play(mMapNewAnimator).with(mToolBarAnimator);
        set.start();
    }

    private void showBtn() {
        mIvRole.setVisibility(View.GONE);
        mPhoneRoot.setVisibility(View.GONE);
        mLuntanRoot.setVisibility(View.GONE);
        mLive2dRoot.setVisibility(View.GONE);
        mRefreshRoot.setVisibility(View.GONE);
        mUserImageRoot.setVisibility(View.GONE);
        mMapNew.setVisibility(View.GONE);
        mLlToolBar.setVisibility(View.VISIBLE);
    }

    private void hideBtn() {
        mIvRole.setVisibility(View.INVISIBLE);
        mPhoneRoot.setVisibility(View.INVISIBLE);
        mLuntanRoot.setVisibility(View.INVISIBLE);
        mLive2dRoot.setVisibility(View.INVISIBLE);
        mRefreshRoot.setVisibility(View.INVISIBLE);
        mUserImageRoot.setVisibility(View.INVISIBLE);
        mMapNew.setVisibility(View.INVISIBLE);
        mLlToolBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mIvRole.isSelected()) {
            clickRole();
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBackTime > 2000) {
            showToast(R.string.msg_click_twice_to_exit);
            mLastBackTime = currentTime;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    protected void onDestroy() {
        MoeMoeApplication.getInstance().removeWindowMager();
        if (mPresenter != null) mPresenter.release();
        if (isGisterReciver) {
            unregisterReceiver(mReceiver);
            isGisterReciver = false;
        }
        EventBus.getDefault().unregister(this);
        MapToolTipUtils.getInstance().release();
        FileDownloader.getImpl().pauseAll();
        FileDownloader.getImpl().unBindService();
        AppSetting.isRunning = false;
        if (initDisposable != null && !initDisposable.isDisposed()) initDisposable.dispose();
        if (resolvDisposable != null && !resolvDisposable.isDisposed()) resolvDisposable.dispose();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        RongIM.getInstance().disconnect();
        if (locationManager != null) locationManager.removeUpdates(this);
        //if(mOrientationListener != null) mOrientationListener.disable();
        super.onDestroy();
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (mUpdateDownloadId == downId) {
                    String apDir = getExternalFilesDir("").getAbsolutePath();
                    Intent installIntent = new Intent();
                    installIntent.setAction(Intent.ACTION_VIEW);
                    File file = new File(apDir, updateApkName);
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(installIntent);
                }
            }
        }
    };

    /**
     * 扒边小人的数据
     *
     * @param entity
     */
    @Override
    public void onLoadHousUserDeskmateSuccess(UserDeskmateEntity entity) {
        deskmateEntity = entity;
        final ArrayList<DeskmateEntils> res = new ArrayList<>();
        final ArrayList<DeskmateEntils> errorList = new ArrayList<>();
        if (entity != null) {
            ArrayList<DeskmateImageEntity> pics = entity.getPics();
            if (pics != null && pics.size() > 0) {
                MapUtil.checkAndDownloadDeskmate(this, true, DeskmateEntils.toDb(pics, "HousUser"), "HousUser", new Observer<DeskmateEntils>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        initDisposable = d;
                    }

                    @Override
                    public void onNext(DeskmateEntils deskmateUserEntils) {
                        File file = new File(StorageUtils.getHouseRootPath() + deskmateUserEntils.getFileName());
                        String md5 = deskmateUserEntils.getMd5();
                        if (md5.length() < 32) {
                            int n = 32 - md5.length();
                            for (int i = 0; i < n; i++) {
                                md5 = "0" + md5;
                            }
                        }
                        if (deskmateUserEntils.getDownloadState() == 3 || !md5.equals(StringUtils.getFileMD5(file))) {
                            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + deskmateUserEntils.getFileName());
                            errorList.add(deskmateUserEntils);
                        } else {
                            res.add(deskmateUserEntils);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao().insertOrReplaceInTx(res);
                        int i = MoeMoeApplication.getInstance().goGreenDao();
                        if (i == 5) {
                            if (MoeMoeApplication.getInstance().isWindow()) {
                                MoeMoeApplication.getInstance().activities.add(MapActivity.this);
                                MoeMoeApplication.getInstance().initWindowManager(MapActivity.this, getWindowManager());
                            }
                        }
                        if (errorList.size() > 0) {
                            resolvErrorListDeskmate(errorList, "HousUser");
                        }
                    }
                });
            }

        }
    }

    @Override
    protected void restartApp() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                restartApp();
                break;
        }
    }

    @Override
    public void onCountChanged(int i) {
        PreferenceUtils.setRCDotNum(this, i);
        int dotNum = PreferenceUtils.getGroupDotNum(this) + i + PreferenceUtils.getJuQIngDotNum(this);
        if (dotNum > 0) {
            if (dotNum > 999) dotNum = 999;
            mTvMsg.setText(dotNum + "条新聊天");
            mTvMsg.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_inform_reddot), null, null, null);
            mTvMsg.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x4));
        } else {
            mTvMsg.setText("无新聊天");
            mTvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mTvMsg.setCompoundDrawablePadding(0);
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (MoeMoeApplication.getInstance().isMenu()) {
//            MoeMoeApplication.getInstance().GoneMenu();
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        if (TencentLocation.ERROR_OK == error) {
            UserLocationEntity entity = new UserLocationEntity(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            mPresenter.saveUserLocation(entity);
            AppSetting.LAT = entity.lat;
            AppSetting.LON = entity.lon;
            mPresenter.loadMapNearUser(entity.lat, entity.lon);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mapEevent(MapEevent event) {
        if (PreferenceUtils.isLogin()) {
            AlertDialogUtil.getInstance().dismissDialog();
        }
//        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
//            mIvPresonal.setImageResource(R.drawable.bg_default_circle);
//        } else {
//            int size = (int) getResources().getDimension(R.dimen.x64);
//            Glide.with(this)
//                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
//                    .error(R.drawable.bg_default_circle)
//                    .placeholder(R.drawable.bg_default_circle)
//                    .bitmapTransform(new CropCircleTransformation(this))
//                    .into(mIvPresonal);
//        }
    }
}