package com.moemoe.lalala.view.widget.video;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DanmakuSend;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.VideoConfig;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 *
 * Created by yi on 2018/2/5.
 */

public class KiraVideoPlayerV2 extends StandardGSYVideoPlayer{

    private DanmakuContext danmakuContext;
    private String danmuUrl;
    private BaseDanmakuParser danmakuParser;
    private DanmakuView danmakuView;
    private View mMenu;

    private View danmakuSettingLayout;
    private View definitionSettingLayout;
    private TextView definitionChange;
    private View switchDanmaku;
    private View danmukuSetting;
    private TextView definitionSetting;

    private View closeSend;
    private View sendDanmaku;
    private EditText danmakuEdit;
    private View sendDanmakuLayout;
    private View sendDanmakuText;
    private boolean isShowDanmu = true;
    private FrameLayout danmakuContainer;
    private boolean needCorrectDanmu;
    private int mLastState = -1;
    private View bigDanmu;
    private View midDanmu;
    private View smallDanmu;
    private ProgressBar mDialogBrightnessProgressBar;

    public KiraVideoPlayerV2(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public KiraVideoPlayerV2(Context context) {
        super(context);
    }

    public KiraVideoPlayerV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        if(mIfCurrentIsFullscreen){
            return R.layout.item_video_land;
        }
        return R.layout.item_video;
    }

    @Override
    protected int getVolumeLayoutId() {
        return R.layout.kira_video_volume_dialog;
    }

    @Override
    protected int getBrightnessLayoutId() {
        return R.layout.kira_video_brightness;
    }

    @Override
    protected int getBrightnessTextId() {
        return R.id.brightness_progressbar;
    }

    @Override
    protected void showBrightnessDialog(float percent) {
        if (mBrightnessDialog == null) {
            View localView = LayoutInflater.from(getActivityContext()).inflate(getBrightnessLayoutId(), null);
            if (localView.findViewById(getBrightnessTextId()) instanceof ProgressBar) {
                mDialogBrightnessProgressBar = localView.findViewById(getBrightnessTextId());
            }
            mBrightnessDialog = new Dialog(getActivityContext(), com.shuyu.gsyvideoplayer.R.style.video_style_dialog_progress);
            mBrightnessDialog.setContentView(localView);
            mBrightnessDialog.getWindow().addFlags(8);
            mBrightnessDialog.getWindow().addFlags(32);
            mBrightnessDialog.getWindow().addFlags(16);
            mBrightnessDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mBrightnessDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER;
            localLayoutParams.width = getWidth();
            localLayoutParams.height = getHeight();
            int location[] = new int[2];
            getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            mBrightnessDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mBrightnessDialog.isShowing()) {
            mBrightnessDialog.show();
        }
        if (mDialogBrightnessProgressBar != null) {
            mDialogBrightnessProgressBar.setProgress((int) (percent * 100));
        }
    }

    @Override
    protected void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initView();
    }

    public View getMenu(){
        return mMenu;
    }

    private void initView(){
        //初始化弹幕控件

        mMenu = findViewById(R.id.iv_menu_list);
        danmakuContainer = findViewById(R.id.danmaku_container);
        if(mIfCurrentIsFullscreen){
            definitionChange = findViewById(R.id.tv_change_definition);
            danmakuSettingLayout = findViewById(R.id.ll_danmu_setting_root);
            definitionSettingLayout = findViewById(R.id.fl_definition_setting_root);
            danmukuSetting = findViewById(R.id.setting_danmu);
            switchDanmaku = findViewById(R.id.switchDanmu);
            danmukuSetting.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if(danmakuSettingLayout.getVisibility() == VISIBLE){
                        hideDanmakuSetting();
                    }else {
                        showDamakuSetting();
                    }
                }
            });

            definitionSetting = findViewById(R.id.definition);
            definitionSetting.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //showDefinitionSetting();
                    ToastUtils.showShortToast(getContext(),"暂不支持");
                }
            });


            //顶部发送弹幕
            sendDanmakuLayout = findViewById(R.id.fl_send_danmu_root);
            closeSend = findViewById(R.id.iv_send_close);
            danmakuEdit = findViewById(R.id.et_danmu);
            sendDanmakuText = findViewById(R.id.text_send_danmu);
            sendDanmaku= findViewById(R.id.tv_send_danmu);

            sendDanmakuText.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if(sendDanmakuLayout.getVisibility() == VISIBLE){
                        sendDanmakuLayout.setVisibility(GONE);
                    }else {
                        hideAllWidget();
                        danmakuEdit.setText("");
                        sendDanmakuLayout.setVisibility(VISIBLE);
                        danmakuEdit.requestFocus();
                        SoftKeyboardUtils.showSoftKeyboard(getContext(),danmakuEdit);
                        cancelDismissControlViewTimer();
                    }
                }
            });


            danmakuEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String danmu = danmakuEdit.getText().toString();
                    if(!TextUtils.isEmpty(danmu)){
                        sendDanmu(danmu);
                    }
                    return false;
                }
            });

            sendDanmaku.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    String danmu = danmakuEdit.getText().toString();
                    if(!TextUtils.isEmpty(danmu)){
                        sendDanmu(danmu);
                    }
                    danmakuEdit.setText("");
                }
            });

            closeSend.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    hideAllWidget();
                    SoftKeyboardUtils.dismissSoftKeyboard(getContext(),danmakuEdit);
                }
            });

            switchDanmaku.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    isShowDanmu = !isShowDanmu;
                    showDamaku(isShowDanmu);
                }
            });

            bigDanmu = findViewById(R.id.tv_big_danmu);
            midDanmu = findViewById(R.id.tv_mid_danmu);
            smallDanmu = findViewById(R.id.tv_small_danmu);

            float curSize = VideoConfig.loadDanmuSize(getContext());
            if(curSize == getResources().getDimension(R.dimen.x40)){
                bigDanmu.setSelected(true);
            }else if(curSize == getResources().getDimension(R.dimen.x30)){
                midDanmu.setSelected(true);
            }else {
                smallDanmu.setSelected(true);
            }

            bigDanmu.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x40));
                    configDanmakuStyle();
                    bigDanmu.setSelected(true);
                    midDanmu.setSelected(false);
                    smallDanmu.setSelected(false);
                }
            });
            midDanmu.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x30));
                    configDanmakuStyle();
                    bigDanmu.setSelected(false);
                    midDanmu.setSelected(true);
                    smallDanmu.setSelected(false);
                }
            });
            smallDanmu.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x20));
                    configDanmakuStyle();
                    bigDanmu.setSelected(false);
                    midDanmu.setSelected(false);
                    smallDanmu.setSelected(true);
                }
            });
            SeekBar seekBar = findViewById(R.id.seek_danmu);
            seekBar.setProgress((int) (VideoConfig.loadDanmuOpacity() * 100));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    VideoConfig.saveDanmuOpacity(progress);
                    configDanmakuStyle();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private void showDamakuSetting(){
        danmakuSettingLayout.setVisibility(VISIBLE);
        cancelDismissControlViewTimer();
    }

    private void hideDanmakuSetting(){
        danmakuSettingLayout.setVisibility(GONE);
    }

    private void showDefinitionSetting(){
        definitionSettingLayout.setVisibility(VISIBLE);
        cancelDismissControlViewTimer();
    }

    private void hideDefinitionSetting(){
        definitionSettingLayout.setVisibility(GONE);
    }

        //修改弹幕样式
    private void configDanmakuStyle(){
        danmakuContext.setDanmakuTransparency(VideoConfig.loadDanmuOpacity());
        danmakuContext.setScaleTextSize(VideoConfig.loadDanmuSize(getContext()));
    }

    public void sendDanmu(String content){

        if(danmakuView != null){
            BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL,danmakuContext);
            if(danmaku == null) return;
            danmaku.text = content;
            danmaku.padding = 5;
            danmaku.priority = 8;
            danmaku.isLive = false;
            danmaku.setTime(danmakuView.getCurrentTime() + 500);
            danmaku.textSize = 1.0f;
            danmaku.textColor = Color.WHITE;
            danmaku.textShadowColor = Color.WHITE;
            danmaku.borderColor = ContextCompat.getColor(getContext(),R.color.main_cyan);
            danmakuView.addDanmaku(danmaku);
        }
        DanmakuSend send = new DanmakuSend();
        send.videoTime = String.valueOf((danmakuView.getCurrentTime() + 500) / 1000f);
        send.content = content;
        send.color = String.valueOf(Color.WHITE);
        send.fontSize = String.valueOf(VideoConfig.loadDanmuSize(getContext()));

        ((DanmuPlayerHolder)CommonUtil.getAppCompActivity(getContext())).onSendDanmu(send);
        hideAllWidget();
        SoftKeyboardUtils.dismissSoftKeyboard(CommonUtil.getAppCompActivity(getContext()));
    }

    public void setUpDanmu(String uri){
        danmuUrl = uri;
        danmakuView = new DanmakuView(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        danmakuContainer.removeAllViews();
        danmakuContainer.addView(danmakuView,lp);

        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL,true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP,true);

        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN,3f)
                .setDuplicateMergingEnabled(false)
                .preventOverlapping(overlappingEnablePair)
                .setScaleTextSize(1.0f)
                .setScrollSpeedFactor(1.2f)
                .setDanmakuTransparency(VideoConfig.loadDanmuOpacity());

        if(!mIfCurrentIsFullscreen){
            HashMap<Integer,Integer> map = new HashMap<>();
            map.put(BaseDanmaku.TYPE_SCROLL_RL,5);
            danmakuContext.setMaximumLines(map);
        }

        danmakuParser = createParser(uri);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
               if(danmakuView != null){
                    danmakuView.start();
                    seekDanmu();
                    if(getCurrentState() != GSYVideoPlayer.CURRENT_STATE_PLAYING){
                        danmakuView.pause();
                    }
               }
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuView.prepare(danmakuParser,
                danmakuContext);
        danmakuView.enableDanmakuDrawingCache(true);

       configDanmakuStyle();
    }

    private void seekDanmu(){
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.seekTo((long) getCurrentPositionWhenPlaying());
        }
    }

    private BaseDanmakuParser createParser(String uri){
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        try {
            loader.load(uri);
        }catch (IllegalDataException e){
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
    }


    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        if(danmakuView != null){
            danmakuView.hide();
        }
        GSYBaseVideoPlayer player = super.startWindowFullscreen(context, actionBar, statusBar);
        if(player != null){
            KiraVideoPlayerV2 videoPlayerV2 = (KiraVideoPlayerV2) player;
            if(!TextUtils.isEmpty(danmuUrl)){
                videoPlayerV2.showDamaku(isShowDanmu);
                videoPlayerV2.isShowDanmu = isShowDanmu;
                videoPlayerV2.setUpDanmu(danmuUrl);
            }
        }
        return player;
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        CommonUtil.hideSupportActionBar(getContext(),true,true);
        if(gsyVideoPlayer != null){
            KiraVideoPlayerV2 gsyDanmaVideoPlayer = (KiraVideoPlayerV2) gsyVideoPlayer;
            if (gsyDanmaVideoPlayer.danmakuView != null &&
                    gsyDanmaVideoPlayer.danmakuView.isPrepared()) {
                showDamaku(gsyDanmaVideoPlayer.isShowDanmu);
                configDanmakuStyle();
            }
        }
    }

    public void onVideoDestroy(){
        if(danmakuView != null){
            danmakuView.release();
        }
    }

    @Override
    protected void setTextAndProgress(int secProgress) {
        super.setTextAndProgress(secProgress);
        if(needCorrectDanmu){
            needCorrectDanmu = false;
            seekDanmu();
        }
        switch (mCurrentState){
            case GSYVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START:
            case GSYVideoPlayer.CURRENT_STATE_PAUSE:
                if(mLastState == mCurrentState){
                    pauseDanmu();
                }
                break;
            case GSYVideoPlayer.CURRENT_STATE_PLAYING:
                if(mLastState != mCurrentState){
                    resumeDanmu();
                }
        }
        mLastState = mCurrentState;
    }

    private void showDamaku(boolean show){
        isShowDanmu = show;
        if(danmakuView != null && danmakuView.isPrepared()){
            if(switchDanmaku != null ) switchDanmaku.setSelected(!show);
            if(show){
                danmakuView.show();
            }else {
                danmakuView.hide();
            }
        }
    }

    @Override
    protected void onClickUiToggle() {
        super.onClickUiToggle();
        if(mIfCurrentIsFullscreen){
            if(danmakuSettingLayout != null && danmakuSettingLayout.getVisibility() == VISIBLE) {
                danmakuSettingLayout.setVisibility(GONE);
            }
            if(definitionSettingLayout != null && definitionSettingLayout.getVisibility() == VISIBLE) definitionSettingLayout.setVisibility(GONE);
            if(sendDanmakuLayout != null && sendDanmakuLayout.getVisibility() == VISIBLE) sendDanmakuLayout.setVisibility(GONE);
        }
    }

    @Override
    protected void hideAllWidget() {
        super.hideAllWidget();
        CommonUtil.hideSupportActionBar(getContext(),true,true);
        if(mIfCurrentIsFullscreen){
            if(danmakuSettingLayout != null) danmakuSettingLayout.setVisibility(GONE);
            if(definitionSettingLayout != null) definitionSettingLayout.setVisibility(GONE);
            if(sendDanmakuLayout != null) sendDanmakuLayout.setVisibility(GONE);
        }
    }

    @Override
    public void onVideoPause() {
        super.onVideoPause();
        pauseDanmu();
    }

    @Override
    public void onVideoResume() {
        super.onVideoResume();
        resumeDanmu();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        pauseDanmu();
        ((DanmuPlayerHolder)CommonUtil.getAppCompActivity(getContext())).onComplete();
    }


    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        pauseDanmu();
        ((DanmuPlayerHolder)CommonUtil.getAppCompActivity(getContext())).onComplete();
    }

    @Override
    public void onSeekComplete() {
        super.onSeekComplete();
        needCorrectDanmu = true;
    }

    private void resumeDanmu(){
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.resume();
        }
    }

    private void pauseDanmu(){
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView){
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                mStartButton.setSelected(true);
            }else if (mCurrentState == CURRENT_STATE_ERROR) {
                mStartButton.setSelected(false);
            }else {
                mStartButton.setSelected(false);
            }
        }else {
            super.updateStartImage();
        }
    }

    @Override
    protected void lockTouchLogic() {
        if (mLockCurScreen) {
            mLockScreen.setImageResource(R.drawable.unlock);
            mLockCurScreen = false;
        } else {
            mLockScreen.setImageResource(R.drawable.lock);
            mLockCurScreen = true;
            hideAllWidget();
        }
    }

    public interface DanmuPlayerHolder {
        void onSendDanmu(DanmakuSend send);
        void onSavePlayHistory(int position);
        void onComplete();
    }
}
