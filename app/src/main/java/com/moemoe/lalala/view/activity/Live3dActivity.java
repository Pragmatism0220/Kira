package com.moemoe.lalala.view.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerLive2dComponent;
import com.moemoe.lalala.di.modules.Live2dModule;
import com.moemoe.lalala.galgame.FileManager;
import com.moemoe.lalala.galgame.Live2DDefine;
import com.moemoe.lalala.galgame.Live2DManager;
import com.moemoe.lalala.galgame.Live2DView;
import com.moemoe.lalala.galgame.SoundManager;
import com.moemoe.lalala.greendao.gen.AlarmClockEntityDao;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.AlarmClockEntity;
import com.moemoe.lalala.model.entity.HouseSleepEntity;
import com.moemoe.lalala.model.entity.Live2dMusicEntity;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.model.entity.PayReqEntity;
import com.moemoe.lalala.model.entity.PayResEntity;
import com.moemoe.lalala.model.entity.ShareLive2dEntity;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.data.model.MusicListType;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.netamusic.player.Notifier;
import com.moemoe.lalala.netamusic.player.OnPlayerEventListener;
import com.moemoe.lalala.netamusic.player.PlayModeEnum;
import com.moemoe.lalala.presenter.Live2dContract;
import com.moemoe.lalala.presenter.Live2dPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.IpAdressUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.Utils;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.schedulers.Schedulers;

/**
 * 官方陪睡Live3d
 * Created by yi on 2018/06/23.
 */

public class Live3dActivity extends BaseAppCompatActivity implements Live2dContract.View, OnPlayerEventListener {

    @BindView(R.id.fl_container)
    FrameLayout mLive2DLayout;
    @BindView(R.id.iv_sound_load)
    ImageView mIvSoundLoad;
    @BindView(R.id.tv_text_1)
    View mTv1;
    @BindView(R.id.tv_text_2)
    View mTv2;
    @BindView(R.id.tv_text_3)
    View mTv3;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.iv_live_len)
    ImageView mIvLen;
    @BindView(R.id.iv_live_sari)
    ImageView mIvSari;
    @BindView(R.id.iv_live_mei)
    ImageView mIvMei;
    @BindView(R.id.iv_live_ichigo)
    ImageView mIvIchiGo;
    @BindView(R.id.iv_live_fuzi)
    ImageView mFuzi;
    @BindView(R.id.iv_live_misaka)
    ImageView mMisaka;
    @BindView(R.id.iv_live_rem)
    ImageView mRem;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.iv_next)
    ImageView mIvNext;
    @BindView(R.id.tv_music_name)
    TextView mTvMusicName;
    @BindView(R.id.horizontal)
    HorizontalScrollView mScrollView;
    @BindView(R.id.rl_diantai)
    RelativeLayout mRlDianTai;

    @Inject
    Live2dPresenter mPresenter;

    private Live2DManager live2DMgr;
    private Live2DView mLive2dView;
    private ObjectAnimator mSoundLoadAnim;
    private String mCurRole;
    // private Player mPlayer;
    // private PlayList playList;
    private ArrayList<Music> musicList;
    private ArrayList<ShareLive2dEntity> mEntites;
    private int mPreMode;
    private boolean isFirstClick = true;
    private String model;
    private ArrayList<HouseSleepEntity> mSleepEntites;
    private OrderEntity entit;
    private BottomMenuFragment bottomFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_live3d;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerLive2dComponent.builder()
                .live2dModule(new Live2dModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        clickEvent("peishui");
        SoundManager.init(this);
        FileManager.init(this);

        musicList = new ArrayList<>();

        model = Live2DDefine.MODEL_LEN;
        mCurRole = "len";
        mIvLen.setAlpha(1.0f);
        mIvSari.setAlpha(0.3f);
        mIvMei.setAlpha(0.3f);
        mIvIchiGo.setAlpha(0.3f);
        mFuzi.setAlpha(0.3f);
        mMisaka.setAlpha(0.3f);
        mRem.setAlpha(0.3f);
//        }
        mTvMusicName.setSelected(true);
        // mPlayer = Player.getInstance(this);
        //  mPlayer.registerCallback(this);
        mPresenter.loadMusicList();
        mPresenter.loadShareLive2dList();
        mPresenter.loadHouseSleep();
        AudioPlayer.get().addOnPlayEventListener(this);
        mPreMode = MusicPreferences.getPlayMode();
        live2DMgr = new Live2DManager(model, false);
        live2DMgr.setOnSoundLoadListener(new Live2DManager.OnSoundLoadListener() {
            @Override
            public void OnStart() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIvSoundLoad.setVisibility(View.VISIBLE);
                        soundLoading();
                    }
                });
            }

            @Override
            public void OnLoad(int count, int position) {

            }

            @Override
            public void OnFinish() {
                if (!isFinishing())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSoundLoadAnim != null) {
                                mSoundLoadAnim.end();
                                mSoundLoadAnim = null;
                            }
                            if (mIvSoundLoad != null) mIvSoundLoad.setVisibility(View.GONE);
                        }
                    });
            }
        });
        mLive2dView = live2DMgr.createView(this);
        mLive2DLayout.addView(mLive2dView, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AlarmClockEntity entity = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao().load(-1L);
        if (entity != null) {
            mTvTime.setText(StringUtils.formatTime(entity
                    .getHour(), entity.getMinute()));
        }

        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams mScrollViewLayoutParams = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();
        mScrollViewLayoutParams.width = (int) (heightPixels - mRlDianTai.getMeasuredWidth() - getResources().getDimension(R.dimen.x50) - getResources().getDimension(R.dimen.status_bar_height));
        mScrollView.setLayoutParams(mScrollViewLayoutParams);
        initPayMenu();
    }

    private void soundLoading() {
        mSoundLoadAnim = ObjectAnimator.ofFloat(mIvSoundLoad, "alpha", 0.2f, 1f).setDuration(300);
        mSoundLoadAnim.setInterpolator(new LinearInterpolator());
        mSoundLoadAnim.setRepeatMode(ValueAnimator.REVERSE);
        mSoundLoadAnim.setRepeatCount(ValueAnimator.INFINITE);
        mSoundLoadAnim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
//        if(mPlayer != null){
//            mPlayer.pause();
//            mPlayer.setPlayList(null);
//            mPlayer.unregisterCallback(this);
//        }
        if (!isFirstClick) {
            AudioPlayer.get().stopPlayer();
            AudioPlayer.get().setShowNotify(true);
            AudioPlayer.get().clearList(false);
            AudioPlayer.get().initList(MusicListType.TYPE_NORMAL);
            MusicPreferences.savePlayMode(mPreMode);
        }
        AudioPlayer.get().removeOnPlayEventListener(this);
        stayEvent("同桌-陪睡模式");
        super.onDestroy();
        SoundManager.release();
        FileManager.release();
    }

    @OnClick({R.id.rl_root_1, R.id.rl_alarm_root, R.id.iv_live_len, R.id.iv_live_mei, R.id.iv_live_sari, R.id.iv_live_ichigo,
            R.id.iv_live_fuzi, R.id.iv_live_misaka, R.id.iv_live_rem,
            R.id.tv_text_1, R.id.tv_text_2, R.id.tv_text_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_root_1:
                if (mTv1.getVisibility() == View.GONE) {
                    mTv1.setVisibility(View.VISIBLE);
                    mTv2.setVisibility(View.VISIBLE);
                    mTv3.setVisibility(View.VISIBLE);
                } else {
                    mTv1.setVisibility(View.GONE);
                    mTv2.setVisibility(View.GONE);
                    mTv3.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_alarm_root:
                final AlarmClockEntity mAlarmClock = new AlarmClockEntity();
                mAlarmClock.setId(-1);
                mAlarmClock.setOnOff(true); // 闹钟默认开启
                mAlarmClock.setRepeat("只响一次");
                mAlarmClock.setWeeks(null);
                if ("len".equals(mCurRole)) {
                    mAlarmClock.setRoleName("小莲");
                } else if ("mei".equals(mCurRole)) {
                    mAlarmClock.setRoleName("美藤双树");
                } else if ("sari".equals(mCurRole)) {
                    mAlarmClock.setRoleName("沙利尔");
                } else if ("ichigo".equals("莓")) {
                    mAlarmClock.setRoleName("莓");
                } else if ("diary_fuzi".equals("斧子")) {
                    mAlarmClock.setRoleName("斧子");
                } else if ("misaka".equals("御坂美琴")) {
                    mAlarmClock.setRoleName("御坂美琴");
                } else if ("rem".equals("蕾姆")) {
                    mAlarmClock.setRoleName("蕾姆");
                }
                mAlarmClock.setRoleId(mCurRole);
                mAlarmClock.setRingName("按时休息");
                mAlarmClock.setRingUrl(R.raw.vc_alerm_len_sleep_1);
                final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                alertDialogUtil.createTimepickerDialog(Live3dActivity.this, mAlarmClock.getHour(), mAlarmClock.getMinute());
                alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                    @Override
                    public void CancelOnClick() {
                        alertDialogUtil.dismissDialog();
                    }

                    @Override
                    public void ConfirmOnClick() {
                        mAlarmClock.setHour(alertDialogUtil.getHour());
                        // 保存闹钟实例的分钟
                        mAlarmClock.setMinute(alertDialogUtil.getMinute());
                        mTvTime.setText(StringUtils.formatTime(alertDialogUtil
                                .getHour(), alertDialogUtil.getMinute()));
                        AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
                        dao.insertOrReplace(mAlarmClock);
                        Utils.startAlarmClock(Live3dActivity.this, mAlarmClock);
                        alertDialogUtil.dismissDialog();
                    }
                });
                alertDialogUtil.showDialog();
                break;
            case R.id.iv_live_len:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("mei".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"mei".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "mei";
                                live2DMgr.changeModel(Live2DDefine.MODEL_LEN);
                                mIvLen.setAlpha(1.0f);
                                mIvSari.setAlpha(0.3f);
                                mIvIchiGo.setAlpha(0.3f);
                                mIvMei.setAlpha(0.3f);
                                mFuzi.setAlpha(0.3f);
                                mMisaka.setAlpha(0.3f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {
                            if (entity.isCompanion()) {
                                if (!"mei".equals(mCurRole)) {
                                    mCurRole = "mei";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_LEN);
                                    mIvLen.setAlpha(1.0f);
                                    mIvSari.setAlpha(0.3f);
                                    mIvIchiGo.setAlpha(0.3f);
                                    mIvMei.setAlpha(0.3f);
                                    mFuzi.setAlpha(0.3f);
                                    mMisaka.setAlpha(0.3f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
//                                showToast(entity.getWhyCannotSleepWithYou());
                                goPayVip();
                            }
                        }
                    }
                }
                break;
            case R.id.iv_live_mei:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("mei".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"mei".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "mei";
                                live2DMgr.changeModel(Live2DDefine.MODEL_MEI);
                                mIvLen.setAlpha(0.3f);
                                mIvSari.setAlpha(0.3f);
                                mIvMei.setAlpha(1.0f);
                                mIvIchiGo.setAlpha(0.3f);
                                mFuzi.setAlpha(0.3f);
                                mMisaka.setAlpha(0.3f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {
                            if (entity.isCompanion()) {
                                if (!"mei".equals(mCurRole)) {
                                    mCurRole = "mei";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_MEI);
                                    mIvLen.setAlpha(0.3f);
                                    mIvSari.setAlpha(0.3f);
                                    mIvMei.setAlpha(1.0f);
                                    mIvIchiGo.setAlpha(0.3f);
                                    mFuzi.setAlpha(0.3f);
                                    mMisaka.setAlpha(0.3f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
//                                showToast(entity.getWhyCannotSleepWithYou());
                                goPayVip();
                            }
                        }
                    }
                }
                break;
            case R.id.iv_live_sari:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("sari".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"sari".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "sari";
                                live2DMgr.changeModel(Live2DDefine.MODEL_SARI);
                                mIvLen.setAlpha(0.3f);
                                mIvSari.setAlpha(1.0f);
                                mIvMei.setAlpha(0.3f);
                                mIvIchiGo.setAlpha(0.3f);
                                mFuzi.setAlpha(0.3f);
                                mMisaka.setAlpha(0.3f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {
                            if (entity.isCompanion()) {
                                if (!"sari".equals(mCurRole)) {
                                    mCurRole = "sari";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_SARI);
                                    mIvLen.setAlpha(0.3f);
                                    mIvSari.setAlpha(1.0f);
                                    mIvMei.setAlpha(0.3f);
                                    mIvIchiGo.setAlpha(0.3f);
                                    mFuzi.setAlpha(0.3f);
                                    mMisaka.setAlpha(0.3f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
//                                showToast(entity.getWhyCannotSleepWithYou());
                                goPayVip();
                            }
                        }

                    }
                }
                break;
            case R.id.iv_live_ichigo:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("ichigo".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"ichigo".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "ichigo";
                                live2DMgr.changeModel(Live2DDefine.MODEL_ICHIGO);
                                mIvLen.setAlpha(0.3f);
                                mIvSari.setAlpha(0.3f);
                                mIvMei.setAlpha(0.3f);
                                mIvIchiGo.setAlpha(1.0f);
                                mFuzi.setAlpha(0.3f);
                                mMisaka.setAlpha(0.3f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {
                            //  showToast("vip才能选哦");
                            if (entity.isCompanion()) {
                                if (!"ichigo".equals(mCurRole)) {
                                    mCurRole = "ichigo";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_ICHIGO);
                                    mIvLen.setAlpha(0.3f);
                                    mIvSari.setAlpha(0.3f);
                                    mIvMei.setAlpha(0.3f);
                                    mIvIchiGo.setAlpha(1.0f);
                                    mFuzi.setAlpha(0.3f);
                                    mMisaka.setAlpha(0.3f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
//                                showToast(entity.getWhyCannotSleepWithYou());
                                goPayVip();
                            }
                        }
                    }
                }
                break;
            case R.id.iv_live_fuzi:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("fuzi".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"fuzi".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "fuzi";
                                live2DMgr.changeModel(Live2DDefine.MODEL_FUZI);
                                mIvLen.setAlpha(0.3f);
                                mIvSari.setAlpha(0.3f);
                                mIvMei.setAlpha(0.3f);
                                mIvIchiGo.setAlpha(0.3f);
                                mFuzi.setAlpha(1.0f);
                                mMisaka.setAlpha(0.3f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {
                            if (entity.isCompanion()) {
                                if (!"fuzi".equals(mCurRole)) {
                                    mCurRole = "fuzi";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_FUZI);
                                    mIvLen.setAlpha(0.3f);
                                    mIvSari.setAlpha(0.3f);
                                    mIvMei.setAlpha(0.3f);
                                    mIvIchiGo.setAlpha(0.3f);
                                    mFuzi.setAlpha(1.0f);
                                    mMisaka.setAlpha(0.3f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
//                                showToast(entity.getWhyCannotSleepWithYou());
                                goPayVip();
                            }
                        }

                    }
                }
                break;
            case R.id.iv_live_misaka:
                if (mSleepEntites != null && mSleepEntites.size() > 0) {
                    HouseSleepEntity entity = null;
                    for (HouseSleepEntity entity1 : mSleepEntites) {
                        if ("misaka".equals(entity1.getRoleOfId())) {
                            entity = entity1;
                        }
                    }
                    if (entity != null) {
                        if (PreferenceUtils.getAuthorInfo().isVip()) {
                            if (!"misaka".equals(mCurRole)) {
                                showToast("咪里马拉轰！神奇的VIP魔法！");
                                mCurRole = "misaka";
                                live2DMgr.changeModel(Live2DDefine.MODEL_MISAKA);
                                mIvLen.setAlpha(0.3f);
                                mIvSari.setAlpha(0.3f);
                                mIvMei.setAlpha(0.3f);
                                mIvIchiGo.setAlpha(0.3f);
                                mFuzi.setAlpha(0.3f);
                                mMisaka.setAlpha(1.0f);
                                mRem.setAlpha(0.3f);
                            }
                        } else {

                            if (entity.isCompanion()) {
                                if (!"misaka".equals(mCurRole)) {
                                    mCurRole = "misaka";
                                    live2DMgr.changeModel(Live2DDefine.MODEL_MISAKA);
                                    mIvLen.setAlpha(0.3f);
                                    mIvSari.setAlpha(0.3f);
                                    mIvMei.setAlpha(0.3f);
                                    mIvIchiGo.setAlpha(0.3f);
                                    mFuzi.setAlpha(0.3f);
                                    mMisaka.setAlpha(1.0f);
                                    mRem.setAlpha(0.3f);
                                }
                            } else {
                                goPayVip();
//                                showToast(entity.getWhyCannotSleepWithYou());
                            }
                        }
                    }
                }
                break;
            case R.id.iv_live_rem:
//                if (mSleepEntites != null && mSleepEntites.size() > 0) {
//                    HouseSleepEntity entity = null;
//                    for (HouseSleepEntity entity1 : mSleepEntites) {
//                        if ("rem".equals(entity1.getRoleOfId())) {
//                            entity = entity1;
//                        }
//                    }
//                    if (entity != null) {
//                        if (entity.isCompanion()) {
//                            if (!"rem".equals(mCurRole)) {
//                                mCurRole = "rem";
//                                live2DMgr.changeModel(Live2DDefine.MODEL_MISAKA);
//                                mIvLen.setAlpha(0.3f);
//                                mIvSari.setAlpha(0.3f);
//                                mIvMei.setAlpha(0.3f);
//                                mIvIchiGo.setAlpha(0.3f);
//                                mFuzi.setAlpha(0.3f);
//                                mMisaka.setAlpha(0.0f);
//                                mRem.setAlpha(1.0f);
//                            }
//                        } else {
////                            showToast(entity.getWhyCannotSleepWithYou());
//                            showToast("正在制作中");
//                        }
//                    }
//                }
                showToast("正在制作中");
                break;
            case R.id.tv_text_1:
            case R.id.tv_text_2:
            case R.id.tv_text_3:
                showToast("功能暂未开放");
                break;
        }
    }

    private void goPayVip() {
        final AlertDialogUtil alertDialogUtilVip = AlertDialogUtil.getInstance();
        alertDialogUtilVip.createNormalDialog(Live3dActivity.this, "这里不可以哦~需要VIP的力量才能进入~");
        alertDialogUtilVip.setOnClickListener(new AlertDialogUtil.OnClickListener() {
            @Override
            public void CancelOnClick() {
                alertDialogUtilVip.dismissDialog();
            }

            @Override
            public void ConfirmOnClick() {
                alertDialogUtilVip.dismissDialog();
                mPresenter.createOrder("d61547ce-62c7-4665-993e-81a78cd32976");
            }
        });
        alertDialogUtilVip.showDialog();
    }

    private void showShareToBuy(String role) {
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle("你被邀请帮助好友解锁妹子的新“姿势”");
        String url = "http://2333.moemoe.la/share/role/" + role + "/" + PreferenceUtils.getUUid();
        oks.setTitleUrl(url);
        oks.setText("青春少女孤枕难眠，点击解锁陪睡姿势" + url);
        oks.setImageUrl("http://s.moemoe.la/ic_shareout.jpg");
        oks.setUrl(url);
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(url);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("role")
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
        oks.show(this);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicList != null && musicList.size() == 0) {
                    showToast("还没有广播歌曲呢");
                } else {
//                   if(AudioPlayer.get().isPlaying()){
//                       mPlayer.pause();
//                       mIvPlay.setImageResource(R.drawable.btn_phone_music_play);
//                   }else {
//                       mPlayer.play();
//                       mIvPlay.setImageResource(R.drawable.btn_phone_music_playing);
//                   }
                    if (isFirstClick) {
                        AudioPlayer.get().stopPlayer();
                        AudioPlayer.get().clearList(false);
                        AudioPlayer.get().addAll(musicList);
                        isFirstClick = false;
                        MusicPreferences.savePlayMode(PlayModeEnum.LOOP.value());
                        AudioPlayer.get().setShowNotify(false);
                        Notifier.get().cancelAll();
                    }
                    AudioPlayer.get().playPause();
                }
            }
        });
        mIvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicList != null && musicList.size() == 0) {
                    showToast("还没有广播歌曲呢");
                } else {
                    AudioPlayer.get().next();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadMusicListSuccess(ArrayList<Live2dMusicEntity> entities) {
        if (entities.size() > 0) mTvMusicName.setText(entities.get(0).getName());
        for (Live2dMusicEntity entity : entities) {
            Music music = new Music();
            music.setType(Music.Type.ONLINE);
            music.setPath(StringUtils.getUrl(entity.getUrl()));
            music.setTitle(entity.getName());
            music.setFileName(entity.getName());
            music.setListType(MusicListType.TYPE_LIVE2D);
            music.setCoverPath("");
            music.setDuration(entity.getTime());
            music.setFileId(entity.getId());
            musicList.add(music);
        }
//        playList = new PlayList();
//        playList.setPlayMode(PlayMode.LOOP);
//        for(Live2dMusicEntity entity : entities){
//            Song mMusicInfo = new Song();
//            mMusicInfo.setPath(ApiService.URL_QINIU + entity.getUrl());
//            mMusicInfo.setDisplayName(entity.getName());
//            mMusicInfo.setDuration(entity.getTime());
//            playList.addSong(mMusicInfo);
//        }
//        mPlayer.setPlayList(playList);
    }

    @Override
    public void onLoadShareListSuccess(ArrayList<ShareLive2dEntity> entities) {
        mEntites = entities;
    }

    @Override
    public void onLoadHouseListSuccess(ArrayList<HouseSleepEntity> entities) {
        mSleepEntites = entities;
    }

    @Override
    public void onCreateOrderSuccess(OrderEntity entity) {
        entit = entity;
        if (bottomFragment != null)
            bottomFragment.show(getSupportFragmentManager(), "payMenu");
    }

    @Override
    public void onPayOrderSuccess(PayResEntity entity) {
        if (entity.isSuccess()) {
            finalizeDialog();
            showToast("支付成功");
            Intent i = new Intent();
            i.putExtra("position", -1);
            i.putExtra("type", "pay");
            setResult(RESULT_OK, i);
            finish();
        } else {
            if (entity.getCharge() != null) {
//                if("qpay".equals(entity.getCharge().get("channel"))){
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString(),"qwallet1104765197");
//                }else {
//                    Pingpp.createPayment(OrderActivity.this, entity.getCharge().toString());
//                }
                PingppUI.createPay(Live3dActivity.this, entity.getCharge().toString(), new PaymentHandler() {
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
                        finalizeDialog();
                        if ("success".equals(result)) {
                            PreferenceUtils.getAuthorInfo().setVip(true);
                            showToast("支付成功");
                            Intent i = new Intent();
                            i.putExtra("position", -1);
                            i.putExtra("type", "pay");
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            showToast(result);
                            finish();
                        }
                    }
                });
            }
        }
    }

    private void initPayMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(0, getString(R.string.label_alipay));
        items.add(item);
        item = new MenuItem(1, getString(R.string.label_wx));
        items.add(item);
        item = new MenuItem(2, getString(R.string.label_qpay));
        items.add(item);
        bottomFragment = new BottomMenuFragment();
        bottomFragment.setShowTop(true);
        bottomFragment.setTopContent("选择支付方式");
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                createDialog();
                String payType = "";
                if (itemId == 0) {
                    payType = "alipay";
                } else if (itemId == 1) {
                    payType = "wx";
                } else if (itemId == 2) {
                    payType = "qpay";
                }
                PayReqEntity entity = new PayReqEntity(entit.getAddress().getAddress(),
                        payType,
                        IpAdressUtils.getIpAdress(Live3dActivity.this),
                        entit.getOrderId(),
                        entit.getAddress().getPhone(),
                        "",
                        entit.getAddress().getUserName());
                mPresenter.payOrder(entity);
            }
        });
    }
//    @Override
//    public void onSwitchLast(@Nullable Song last) {
//
//    }
//
//    @Override
//    public void onSwitchNext(@Nullable Song next) {
//        if(next != null) {
//            mTvMusicName.setText(next.getDisplayName());
//        }else {
//            mTvMusicName.setText("");
//        }
//    }
//
//    @Override
//    public void onComplete(@Nullable Song next) {
//
//    }
//
//    @Override
//    public void onPlayStatusChanged(boolean isPlaying) {
//        if(isPlaying){
//            mIvPlay.setImageResource(R.drawable.btn_phone_music_playing);
//        }else {
//            mIvPlay.setImageResource(R.drawable.btn_phone_music_play);
//        }
//    }

    @Override
    public void onChange(Music music) {
        mTvMusicName.setText(music.getTitle());
    }

    @Override
    public void onPlayerStart() {
        mIvPlay.setImageResource(R.drawable.btn_phone_music_playing);
    }

    @Override
    public void onPlayerPause() {
        mIvPlay.setImageResource(R.drawable.btn_phone_music_play);
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }
}
