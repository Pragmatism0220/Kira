//package com.moemoe.lalala.view.widget.video;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.AppCompatSeekBar;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.moemoe.lalala.R;
//import com.moemoe.lalala.model.entity.DanmakuSend;
//import com.moemoe.lalala.utils.KiraDanmakuParser;
//import com.moemoe.lalala.utils.NoDoubleClickListener;
//import com.moemoe.lalala.utils.SoftKeyboardUtils;
//import com.moemoe.lalala.utils.ToastUtils;
//import com.moemoe.lalala.utils.VideoConfig;
//import com.shuyu.gsyvideoplayer.GSYVideoManager;
//import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
//import com.shuyu.gsyvideoplayer.utils.CommonUtil;
//import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
//import com.shuyu.gsyvideoplayer.video.GSYBaseVideoPlayer;
//import com.shuyu.gsyvideoplayer.video.PreviewGSYVideoPlayer;
//
//import java.util.HashMap;
//import java.util.Locale;
//
//import master.flame.danmaku.controller.DrawHandler;
//import master.flame.danmaku.danmaku.loader.ILoader;
//import master.flame.danmaku.danmaku.loader.IllegalDataException;
//import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
//import master.flame.danmaku.danmaku.model.BaseDanmaku;
//import master.flame.danmaku.danmaku.model.DanmakuTimer;
//import master.flame.danmaku.danmaku.model.IDisplayer;
//import master.flame.danmaku.danmaku.model.android.DanmakuContext;
//import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
//import master.flame.danmaku.ui.widget.DanmakuView;
//
///**
// *
// * Created by yi on 2018/2/1.
// */
//
//public class KiraVideoPlayer extends PreviewGSYVideoPlayer {
//
//    private TextView loadText;
//    private DanmakuContext danmakuContext;
//    private String danmuUrl;
//    private BaseDanmakuParser danmakuParser;
//    private FrameLayout danmakuContainer;
//    private DanmakuView danmakuView;
//    private View mMenu;
//
//    private View danmakuSettingLayout;
//    private View definitionSettingLayout;
//    private TextView definitionChange;
//    private View switchDanmaku;
//    private View danmukuSetting;
//    private TextView definitionSetting;
//
//    private View closeSend;
//    private View sendDanmaku;
//    private EditText danmakuEdit;
//    private View sendDanmakuLayout;
//    private View sendDanmakuText;
//
//    private View jumpLayout;
//    private View closeJump;
//    private TextView jumpTimeText;
//    private View jumpText;
//
//    private int mLastState = -1;
//    private boolean needCorrectDanmu;
//    private boolean isShowDanmu;
//
//    private static final int TAP = 1;
//    private static final long DOUBLE_TAP_TIMEOUT = 250L;
//    private static final long DOUBLE_TAP_MIN_TIME = 40L;
//    private static final long DOUBLE_TAP_SLOP = 100L;
//    private static final long DOUBLE_TAP_SLOP_SQUARE = DOUBLE_TAP_SLOP * DOUBLE_TAP_SLOP;
//
//    private boolean isDoubleTapping = false;
//    private boolean isStillDown = false;
//    private boolean deferConfirmSingleTap = false;
//    private MotionEvent currentDownEvent;
//    private MotionEvent previousUpEvent;
//
//    @SuppressLint("HandlerLeak")
//    private Handler gestureHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case TAP:
//                    if (!isStillDown) {
//                        onSingleTapConfirmed();
//                    } else {
//                        deferConfirmSingleTap = true;
//                    }
//                    break;
//            }
//        }
//    };
//
//
//    public KiraVideoPlayer(Context context, Boolean fullFlag) {
//        super(context, fullFlag);
//    }
//
//    public KiraVideoPlayer(Context context) {
//        super(context);
//    }
//
//    public KiraVideoPlayer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void init(Context context) {
//        super.init(context);
//        initView();
//    }
//
//    @Override
//    public int getLayoutId() {
//        if (mIfCurrentIsFullscreen) {
//            return R.layout.item_video_land;
//        }
//        return R.layout.item_video;
//    }
//
//    public View getMenu(){
//        return mMenu;
//    }
//
//    public TextView getLoadText(){
//        return loadText;
//    }
//
//    private void initView(){
//        //初始化弹幕控件
//        danmakuContainer = findViewById(R.id.danmaku_container);
//
//        jumpLayout = findViewById(R.id.linear_jump);
//        closeJump = findViewById(R.id.img_close_jump);
//        jumpTimeText = findViewById(R.id.text_jump_time);
//        jumpText = findViewById(R.id.text_jump);
//        mMenu = findViewById(R.id.iv_menu_list);
//
//        loadText = findViewById(R.id.text_load);
//
//        if(mIfCurrentIsFullscreen){
//            definitionChange = findViewById(R.id.tv_change_definition);
//            danmakuSettingLayout = findViewById(R.id.ll_danmu_setting_root);
//            definitionSettingLayout = findViewById(R.id.fl_definition_setting_root);
//            danmukuSetting = findViewById(R.id.setting_danmu);
//            switchDanmaku = findViewById(R.id.switchDanmu);
//            danmukuSetting.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    showDamakuSetting();
//                }
//            });
//
//            definitionSetting = findViewById(R.id.definition);
//            definitionSetting.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    //showDefinitionSetting();
//                    ToastUtils.showShortToast(getContext(),"暂不支持");
//                }
//            });
//
//
//            //顶部发送弹幕
//            sendDanmakuLayout = findViewById(R.id.fl_send_danmu_root);
//            closeSend = findViewById(R.id.iv_send_close);
//            danmakuEdit = findViewById(R.id.et_danmu);
//            sendDanmakuText = findViewById(R.id.text_send_danmu);
//            sendDanmaku= findViewById(R.id.tv_send_danmu);
//
//            sendDanmakuText.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    if(sendDanmakuLayout.getVisibility() == VISIBLE){
//                        sendDanmakuLayout.setVisibility(GONE);
//                    }else {
//                        danmakuEdit.setText("");
//                        sendDanmakuLayout.setVisibility(VISIBLE);
//                        danmakuEdit.requestFocus();
//                        SoftKeyboardUtils.showSoftKeyboard(getContext(),danmakuEdit);
//                        cancelDismissControlViewTimer();
//                    }
//                }
//            });
//
//
//            danmakuEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    String danmu = danmakuEdit.getText().toString();
//                    if(!TextUtils.isEmpty(danmu)){
//                        sendDanmu(danmu);
//                    }
//                    return false;
//                }
//            });
//
//            sendDanmaku.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    String danmu = danmakuEdit.getText().toString();
//                    if(!TextUtils.isEmpty(danmu)){
//                        sendDanmu(danmu);
//                    }
//                }
//            });
//
//            closeSend.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    hideAllWidget();
//                    SoftKeyboardUtils.dismissSoftKeyboard(getContext(),danmakuEdit);
//                }
//            });
//
//            switchDanmaku.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    showDamaku(!isShowDanmu);
//                }
//            });
//
//            findViewById(R.id.tv_big_danmu).setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x40));
//                    configDanmakuStyle();
//                }
//            });
//            findViewById(R.id.tv_mid_danmu).setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x30));
//                    configDanmakuStyle();
//                }
//            });
//            findViewById(R.id.tv_small_danmu).setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    VideoConfig.saveDanmuSize(getContext().getResources().getDimension(R.dimen.x20));
//                    configDanmakuStyle();
//                }
//            });
//            AppCompatSeekBar seekBar = findViewById(R.id.seek_danmu);
//            seekBar.setProgress(VideoConfig.loadDanmuOpacity());
//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    int pro = getOpacityReal(progress);
//                    seekBar.setProgress(pro);
//                    VideoConfig.saveDanmuOpacity(pro);
//                    configDanmakuStyle();
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//        }
//
//
//        closeJump.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                hideJump();
//            }
//        });
//
//        loadText.setVisibility(VISIBLE);
//    }
//
//    private int getOpacityReal(int progress){
//        if(progress <= 0){
//            return 0;
//        }else if(progress > 0 || progress <= 20){
//            return 20;
//        }else if(progress > 20 || progress <= 40){
//            return 40;
//        }else if(progress > 40 || progress <= 60){
//            return 60;
//        }else if(progress > 60 || progress <= 80){
//            return 80;
//        }else{
//            return 100;
//        }
//
//    }
//
//    private void showDamaku(boolean show){
//        isShowDanmu = show;
//        if(danmakuView != null && danmakuView.isPrepared()){
//            switchDanmaku.setSelected(!show);
//            if(show){
//                danmakuView.show();
//            }else {
//                danmakuView.hide();
//            }
//        }
//    }
//
//    private void showDamakuSetting(){
//        danmakuSettingLayout.setVisibility(VISIBLE);
//        cancelDismissControlViewTimer();
//    }
//
//    private void hideDanmakuSetting(){
//        danmakuSettingLayout.setVisibility(GONE);
//    }
//
//    private void showDefinitionSetting(){
//        definitionSettingLayout.setVisibility(VISIBLE);
//        cancelDismissControlViewTimer();
//    }
//
//    private void hideDefinitionSetting(){
//        definitionSettingLayout.setVisibility(GONE);
//    }
//
//    public void showJump(final int position){
//        int minute = position / 1000 / 60;
//        int seconds = position / 1000 % 60;
//        jumpTimeText.setText(String.format(Locale.getDefault(),"记忆您上次播放到%d:%02d",minute,seconds));
//        jumpText.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                GSYVideoManager.instance().getMediaPlayer().seekTo((long) position);
//                hideJump();
//            }
//        });
//        jumpLayout.setVisibility(VISIBLE);
//    }
//
//    public void hideJump(){
//        jumpLayout.setVisibility(GONE);
//    }
//
//    //修改弹幕样式
//    private void configDanmakuStyle(){
//        danmakuContext.setDanmakuTransparency(VideoConfig.loadDanmuOpacity());
//        danmakuContext.setScaleTextSize(VideoConfig.loadDanmuSize(getContext()));
//    }
//
//    public void sendDanmu(String content){
//        DanmakuSend send = new DanmakuSend();
//        send.videoTime = String.valueOf(getCurrentPositionWhenPlaying() /1000f);
//        send.content = content;
//        send.color = "ffffff";
//        send.fontSize = String.valueOf(VideoConfig.loadDanmuSize(getContext()));
//        ((DanmuPlayerHolder)getContext()).onSendDanmu(send);
//
//        if(danmakuView != null){
//            BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
//            danmaku.text = content;
//            danmaku.padding = 5;
//            danmaku.priority = 1;
//            danmaku.isLive = false;
//            danmaku.setTime(danmakuView.getCurrentTime() + 1200);
//            danmaku.textSize = VideoConfig.loadDanmuSize(getContext());
//            danmaku.textColor = Color.WHITE;
//            danmaku.textShadowColor = Color.WHITE;
//            danmaku.borderColor = ContextCompat.getColor(getContext(),R.color.main_cyan);
//            pauseDanmu();
//            danmakuView.addDanmaku(danmaku);
//            resumeDanmu();
//        }
//        hideAllWidget();
//        SoftKeyboardUtils.dismissSoftKeyboard(CommonUtil.getAppCompActivity(getContext()));
//    }
//
//    public void setOrientationUtils(OrientationUtils orientationUtils){
//        mOrientationUtils = orientationUtils;
//    }
//
//    public OrientationUtils getOrientationUtils(){
//        return mOrientationUtils;
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if(mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull){
//            return super.onTouch(v, event);
//        }
//
//        if(v.getId() == R.id.surface_container){
//            switch (event.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    boolean hadTapMessage = gestureHandler.hasMessages(TAP);
//                    if(hadTapMessage){
//                        gestureHandler.removeMessages(TAP);
//                    }
//                    if(currentDownEvent != null && previousUpEvent != null && hadTapMessage &&
//                            isConsideredDoubleTap(currentDownEvent,previousUpEvent,event)){
//                        isDoubleTapping = true;
//                    }else {
//                        gestureHandler.sendEmptyMessageDelayed(TAP,DOUBLE_TAP_TIMEOUT);
//                    }
//                    if(currentDownEvent != null){
//                        currentDownEvent.recycle();
//                    }
//                    currentDownEvent = MotionEvent.obtain(event);
//
//                    isStillDown = true;
//                    deferConfirmSingleTap = false;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    isStillDown = false;
//                    if(isDoubleTapping){
//                        onDoubleTap();
//                    }else {
//                        onSingleTapConfirmed();
//                    }
//                    if(previousUpEvent != null){
//                        previousUpEvent.recycle();
//                    }
//                    previousUpEvent = MotionEvent.obtain(event);
//                    isDoubleTapping = false;
//                    deferConfirmSingleTap = false;
//                    break;
//            }
//        }
//
//        return super.onTouch(v, event);
//    }
//
//    private boolean isConsideredDoubleTap(MotionEvent firstDown,MotionEvent firstUp,MotionEvent secondDown){
//        long l = secondDown.getEventTime() - firstUp.getEventTime();
//        if(l > DOUBLE_TAP_TIMEOUT || l < DOUBLE_TAP_MIN_TIME){
//            return false;
//        }
//        float deltaX = firstDown.getX() - secondDown.getX();
//        float deltaY = firstDown.getY() - secondDown.getY();
//        return (deltaX * deltaX + deltaY * deltaY < DOUBLE_TAP_SLOP_SQUARE);
//    }
//
//    private void onDoubleTap(){
//        if(getCurrentState() == GSYVideoPlayer.CURRENT_STATE_PLAYING || getCurrentState() == GSYVideoPlayer.CURRENT_STATE_PAUSE){
//            mStartButton.performClick();
//        }
//    }
//
//    private void onSingleTapConfirmed(){
//        startDismissControlViewTimer();
//        if(!mChangePosition && !mChangeVolume && !mBrightness){
//            onClickUiToggle();
//        }
//    }
//
//    @Override
//    public boolean setUp(String url) {
//        if(url.startsWith("http")){
//            url = "async:" + url;
//        }
//        return super.setUp(url);
//    }
//
//    public void setUpDanmu(String uri){
//        danmakuView = new DanmakuView(getContext());
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        danmakuContainer.removeAllViews();
//        danmakuContainer.addView(danmakuView,lp);
//
//        danmuUrl = uri;
//        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
//        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL,true);
//        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP,true);
//
//        danmakuContext = DanmakuContext.create();
//        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN,3f)
//                .setDuplicateMergingEnabled(false)
//                .preventOverlapping(overlappingEnablePair)
//                .setScaleTextSize(VideoConfig.loadDanmuSize(getContext()))
//                .setScrollSpeedFactor(1.2f)
//                .setDanmakuTransparency(VideoConfig.loadDanmuOpacity());
//
//        if(!mIfCurrentIsFullscreen){
//            HashMap<Integer,Integer> map = new HashMap<>();
//            map.put(BaseDanmaku.TYPE_SCROLL_RL,5);
//            danmakuContext.setMaximumLines(map);
//        }
//
//        danmakuParser = createParser(uri);
//        danmakuView.setCallback(new DrawHandler.Callback() {
//            @Override
//            public void prepared() {
//                loadText.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadText.setText(loadText.getText().toString().replace("Kira弹幕装填...","Kira弹幕装填...[完成]"));
//                    }
//                });
//                danmakuView.start(getCurrentPositionWhenPlaying());
//                if(getCurrentState() != GSYVideoPlayer.CURRENT_STATE_PLAYING){
//                    danmakuView.pause();
//                }
//            }
//
//            @Override
//            public void updateTimer(DanmakuTimer timer) {
//
//            }
//
//            @Override
//            public void danmakuShown(BaseDanmaku danmaku) {
//
//            }
//
//            @Override
//            public void drawingFinished() {
//
//            }
//        });
////        danmakuView.bindClockProvider(new SystemClock.ClockProvider() {
////            @Override
////            public long updateMillis() {
////                return SystemClock.uptimeMillis();
////            }
////        });
//
//        danmakuView.prepare(danmakuParser,danmakuContext);
//        danmakuView.enableDanmakuDrawingCache(true);
//        configDanmakuStyle();
//    }
//
//    private BaseDanmakuParser createParser(String uri){
//        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
//        try {
//            loader.load(uri);
//        }catch (IllegalDataException  e){
//            e.printStackTrace();
//        }
//        KiraDanmakuParser parser = new KiraDanmakuParser();
//        parser.load(loader.getDataSource());
//        return parser;
//    }
//
//    @Override
//    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
//        if(danmakuView != null){
//            danmakuView.hide();
//        }
//
//        KiraVideoPlayer player = (KiraVideoPlayer) super.startWindowFullscreen(context, actionBar, statusBar);
//        player.setSpeed(getSpeed());
//        if(!TextUtils.isEmpty(danmuUrl)){
//            player.showDamaku(isShowDanmu);
//            player.setUpDanmu(danmuUrl);
//            player.configDanmakuStyle();
//        }
//        return player;
//    }
//
//    @Override
//    protected void resolveFullVideoShow(Context context, GSYBaseVideoPlayer gsyVideoPlayer, FrameLayout frameLayout) {
//        ((KiraVideoPlayer)gsyVideoPlayer).setOrientationUtils(mOrientationUtils);
//        super.resolveFullVideoShow(context, gsyVideoPlayer, frameLayout);
//    }
//
//    @Override
//    protected void updateStartImage() {
//        if(mStartButton instanceof ImageView){
//            if (mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING) {
//                mStartButton.setSelected(true);
//            }else if (mCurrentState == GSYVideoPlayer.CURRENT_STATE_ERROR) {
//                mStartButton.setSelected(false);
//            }else {
//                mStartButton.setSelected(false);
//            }
//        }else {
//            super.updateStartImage();
//        }
//    }
//
//    @Override
//    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
//        if(gsyVideoPlayer != null){
//            showDamaku(((KiraVideoPlayer)gsyVideoPlayer).isShowDanmu);
//            configDanmakuStyle();
//            setSpeed( (gsyVideoPlayer).getSpeed());
//        }
//        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
//    }
//
//    @Override
//    protected void onClickUiToggle() {
//        super.onClickUiToggle();
//        if(mIfCurrentIsFullscreen){
//            if(mBottomContainer.getVisibility() != GONE){
//                if(danmakuSettingLayout.getVisibility() == VISIBLE){
//                    hideDanmakuSetting();
//                }
//                if(definitionSettingLayout.getVisibility() == VISIBLE){
//                    hideDefinitionSetting();
//                }
//                if(sendDanmakuLayout.getVisibility() == VISIBLE){
//                    sendDanmakuLayout.setVisibility(GONE);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void hideAllWidget() {
//        super.hideAllWidget();
//        if(mIfCurrentIsFullscreen){
//            sendDanmakuLayout.setVisibility(GONE);
//            hideDanmakuSetting();
//            hideDefinitionSetting();
//        }
//    }
//
//    public void onVideoPause(boolean isPlay,boolean isComplete){
//        onVideoPause();
//        if(isPlay){
//            if(isComplete){
//                ((DanmuPlayerHolder)CommonUtil.getAppCompActivity(getContext())).onSavePlayHistory(0);
//            }else {
//                ((DanmuPlayerHolder)CommonUtil.getAppCompActivity(getContext())).onSavePlayHistory(getCurrentPositionWhenPlaying());
//            }
//        }
//    }
//
//    @Override
//    public void onVideoPause() {
//        super.onVideoPause();
//        if(danmakuView != null && danmakuView.isPrepared()){
//            danmakuView.pause();
//        }
//    }
//
//    @Override
//    public void onVideoResume() {
//        super.onVideoResume();
//        if(mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING && danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()){
//            danmakuView.resume();
//        }
//    }
//
//    public void onVideoDestroy(){
//        if(danmakuView != null){
//            danmakuView.release();
//        }
//    }
//
//    @Override
//    protected void setTextAndProgress() {
//        super.setTextAndProgress();
//        if(needCorrectDanmu){
//            needCorrectDanmu = false;
//            seekDanmu();
//        }
//        switch (mCurrentState){
//            case GSYVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START:
//            case GSYVideoPlayer.CURRENT_STATE_PAUSE:
//                if(mLastState == mCurrentState){
//                    pauseDanmu();
//                }
//                break;
//            case GSYVideoPlayer.CURRENT_STATE_PLAYING:
//                if(mLastState != mCurrentState){
//                    resumeDanmu();
//                }
//        }
//        mLastState = mCurrentState;
//    }
//
//    @Override
//    public void onPrepared() {
//        super.onPrepared();
//        CommonUtil.hideSupportActionBar(getContext(),false,true);
//    }
//
//    private void resumeDanmu(){
//        if (danmakuView != null && danmakuView.isPrepared()) {
//            danmakuView.resume();
//        }
//    }
//
//    private void pauseDanmu(){
//        if (danmakuView != null && danmakuView.isPrepared()) {
//            danmakuView.pause();
//        }
//    }
//
//    private void seekDanmu(){
//        if (danmakuView != null && danmakuView.isPrepared()) {
//            danmakuView.seekTo((long) getCurrentPositionWhenPlaying());
//        }
//    }
//
//    @Override
//    public void onSeekComplete() {
//        super.onSeekComplete();
//        needCorrectDanmu = true;
//    }
//
//    public interface DanmuPlayerHolder {
//        void onSendDanmu(DanmakuSend send);
//        void onSavePlayHistory(int position);
//    }
//}
